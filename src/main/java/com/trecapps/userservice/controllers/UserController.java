package com.trecapps.userservice.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.userservice.models.LogIn;
import com.trecapps.userservice.models.NewUser;
import com.trecapps.userservice.models.PasswordChange;
import com.trecapps.userservice.models.TrecAccount;
import com.trecapps.userservice.services.TrecAccountService;

@RestController
public class UserController {

	@Autowired
	TrecAccountService accountService;
	
	@Autowired
	AuthenticationManager authManager;
	
	@PostMapping("/CreateUser")
	TrecAccount createUser(@RequestBody NewUser newUser,HttpServletRequest req, HttpServletResponse res)
	{
		System.out.println("Controller:Create User: " + newUser);
		TrecAccount account = accountService.createAccount(newUser);
		authenticateUser(account, req);
		return account;
	}
	
	private void authenticateUser(TrecAccount account,HttpServletRequest req)
	{
		if(account != null)
		{			
			UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());
			Authentication auth = authManager.authenticate(authReq);
			SecurityContext sc = SecurityContextHolder.getContext();
			
			sc.setAuthentication(auth);
			HttpSession session = req.getSession(true);
		    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
		    account.setToken(null);
		}
	}
	
	@GetMapping("/UserExists")
	Boolean userExists(@RequestParam String username)
	{
		return accountService.getAccountByUserName(username) != null;
	}
	
	@PostMapping("/LogIn")
	TrecAccount logIn(@RequestBody LogIn login, HttpServletRequest req)
	{
		TrecAccount account = null;
		if(login.getUsername() != null)
		{
			account = accountService.logInUsername(login.getUsername(), login.getPassword());
		}
		else if(login.getEmail() != null)
		{
			account = accountService.logInEmail(login.getEmail(), login.getPassword());
		}
		
		authenticateUser(account, req);
		return account;
	}
	
	@GetMapping("/Validate")
	Boolean validate()
	{
		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth = sc.getAuthentication();
		if(auth.isAuthenticated())
		{
			String username = auth.getPrincipal().toString();
			TrecAccount account = accountService.getAccountByUserName(username);
			System.out.println("Controller:GetValidate: " + account + " from username " + username);
			accountService.sendverificationEmail(account);
			return true;
		}
		return false;
	}
	
	@PostMapping("/Validate")
	Boolean validateWithToken(@RequestBody String validationToken)
	{
		Authentication authorized = SecurityContextHolder.getContext().getAuthentication();
		
		if(authorized.isAuthenticated())
		{
			String username = authorized.getPrincipal().toString();
			 return accountService.verifyAccount(username, validationToken);
		}
		else
		{
			throw new AuthenticationCredentialsNotFoundException("User Session not authenticated");
		}
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
