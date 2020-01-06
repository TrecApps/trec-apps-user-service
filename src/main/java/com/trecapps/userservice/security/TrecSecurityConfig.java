package com.trecapps.userservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class TrecSecurityConfig extends WebSecurityConfigurerAdapter 
{
	@Autowired
	@Qualifier("TrecAccountService")
	UserDetailsService userDetailsService;
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable().authorizeRequests().antMatchers("/NewUser").permitAll().
			antMatchers("/CreateUser").permitAll().
			antMatchers("/LogIn**").permitAll().
			antMatchers("/*").permitAll().
			antMatchers("/Secure/**").authenticated().
			antMatchers("/UpdateUser**").authenticated().
			antMatchers("/Validate**").authenticated().
			antMatchers("/UpdatePassword**").authenticated().
			and().
			logout().invalidateHttpSession(true).clearAuthentication(true).					
			logoutRequestMatcher(new AntPathRequestMatcher("/Logout")).permitAll(); 
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(userDetailsService);
		
		auth.authenticationProvider(provider);
	}
}
