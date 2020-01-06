package com.trecapps.userservice.security;

import org.springframework.security.core.GrantedAuthority;

public class TrecAuthority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2664263568580241195L;
	/**
	 * 
	 */
	
	private String authority;
	
	public TrecAuthority(String auth)
	{
		authority = auth;
	}
	
	@Override
	public String getAuthority() {
		return authority;
	}

}
