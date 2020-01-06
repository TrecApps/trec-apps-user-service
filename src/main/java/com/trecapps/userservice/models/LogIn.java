package com.trecapps.userservice.models;

import org.springframework.stereotype.Component;

@Component
public class LogIn {
	String username;
	String email;
	String password;
	public LogIn() {
		super();
	}
	public LogIn(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}
	@Override
	public String toString() {
		return "LogIn [username=" + username + ", email=" + email + ", password=" + password + "]";
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
