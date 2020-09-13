package com.trecapps.userservice.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.trecapps.userservice.models.LogIn;
import com.trecapps.userservice.models.NewUser;
import com.trecapps.userservice.models.PasswordChange;
import com.trecapps.userservice.models.ReturnObj;
import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.services.JwtTokenService;
import com.trecapps.userservice.services.TrecAccountService;

@RestController
public class UserController {

	@Autowired
	TrecAccountService accountService;
	
	@Autowired
	JwtTokenService tokenService;
	
	@GetMapping(value="/CreateUser")
	public ModelAndView formNewUser()
	{
		ModelAndView view = new ModelAndView();
		view.setViewName("register");
		return view;
	}
	
	@PostMapping("/CreateUser")
	ResponseEntity<ReturnObj> createUser(RequestEntity<NewUser> entity)
	{
		var body = entity.getBody();
		
		TrecAccount account = accountService.createAccount(body);
		
		if(account == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.BAD_REQUEST);
		
		ReturnObj ret = generateAuth(account);
		
		if(ret == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ReturnObj>(ret, HttpStatus.OK);
	}
	
	private ReturnObj generateAuth(TrecAccount account)
	{
		String token = tokenService.generateToken(account);
		
		if(token == null)
			return null;
		
		return new ReturnObj(token, account.getUsername(), account.getFirstName(), account.getLastName(), account.getColor());
	}
	
	@GetMapping("/UserExists")
	Boolean userExists(@RequestParam String username)
	{
		return accountService.getAccountByUserName(username) != null;
	}
	
	@PostMapping("/LogIn")
	ResponseEntity<ReturnObj> logIn(RequestEntity<LogIn> entity)
	{
		LogIn login = entity.getBody();
		
		if(login == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.BAD_REQUEST);
		
		
		
		TrecAccount account = null;
		if(login.getUsername() != null)
		{
			account = accountService.logInUsername(login.getUsername(), login.getPassword());
		}
		else if(login.getEmail() != null)
		{
			account = accountService.logInEmail(login.getEmail(), login.getPassword());
		}
		
		if(account == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.BAD_REQUEST);
		
		ReturnObj ret = generateAuth(account);
		
		if(ret == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ReturnObj>(ret, HttpStatus.OK);
	}
	
	@GetMapping("/Validate")
	Boolean validate(HttpServletRequest req)
	{
		String token = req.getHeader("Authorization");
		if(token == null)
			return false;
		
		TrecAccount account = tokenService.verifyToken(token);
		
		if(account == null)
			return false;
		
		if(account.getIsValidated() != 0)
			return true;
		
		accountService.sendverificationEmail(account);
		return true;
	}
	
	@PostMapping("/Validate")
	ResponseEntity<String> validateWithToken(HttpServletRequest req)
	{
		String token = req.getHeader("Authorization");
		String validationToken = req.getHeader("Verification");
		
		if(token == null)
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		
		TrecAccount account = tokenService.verifyToken(token);
		
		if(account == null)
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		
		if(validationToken == null)
			return new ResponseEntity<String>("Validation Token not provided in 'Verification' header", HttpStatus.BAD_REQUEST);
		
		account = accountService.verifyAccount(account.getUsername(), validationToken);
		
		if(account == null)
			return new ResponseEntity<String>("Validation Token provided in 'Verification' header not correct", HttpStatus.BAD_REQUEST);
		
		String ret = tokenService.generateToken(account);
		
		if(ret == null)
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		return new ResponseEntity<String>(ret, HttpStatus.OK);
	}
	
	@PutMapping("/UpdateUser")
	Boolean updateUser(@RequestBody TrecAccount account)
	{
		Authentication authorized = SecurityContextHolder.getContext().getAuthentication();
		
		if(authorized.isAuthenticated())
		{
			return accountService.updateUser(account);
			
		}
		return false;
	}
	
	@PutMapping("/UpdatePassword")
	Boolean updatePassword(@RequestBody PasswordChange passwordChange)
	{
		Authentication authorized = SecurityContextHolder.getContext().getAuthentication();
		TrecAccount account;
		if(authorized.isAuthenticated() && (account = accountService.getAccountByUserName(authorized.getPrincipal().toString())) != null)
		{
			return accountService.updatePassword(account.getAccountId(), passwordChange.getCurrentPassword(), passwordChange.getNewPassword())
					!= null;
			
		}
		return false;
	}
	
	@GetMapping("/Logout")
	Boolean logout()
	{
		return true;
	}
}
