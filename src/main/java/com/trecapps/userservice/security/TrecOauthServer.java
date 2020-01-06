package com.trecapps.userservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import com.trecapps.userservice.services.TrecOauthClientService;

@Configuration
@EnableAuthorizationServer
public class TrecOauthServer extends AuthorizationServerConfigurerAdapter
{
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	TrecOauthClientService oauthClientService;
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception 
	{
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
		clients.withClientDetails(oauthClientService);
	}
}
