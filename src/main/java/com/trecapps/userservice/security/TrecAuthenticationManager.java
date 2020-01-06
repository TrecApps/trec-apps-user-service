package com.trecapps.userservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.trecapps.userservice.models.TrecAccount;
import com.trecapps.userservice.services.TrecAccountService;

@Component
public class TrecAuthenticationManager implements AuthenticationManager {


	@Autowired
	TrecAccountService service;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		
		TrecAccount ta = service.logInUsername(username, password);
		
		UsernamePasswordAuthenticationToken token;
		
		if(ta == null)
		{
			// Authentication failed
			token = new UsernamePasswordAuthenticationToken(username, password);
			token.setAuthenticated(false);
			
		}
		else
		{
			// authentication succeeded
			token = new UsernamePasswordAuthenticationToken(username, password, ta.getAuthorities());
		}
		
		return token;
	}

}
