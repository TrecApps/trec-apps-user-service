package com.trecapps.userservice.models;

import org.springframework.stereotype.Component;

@Component
public class PasswordChange {

	String currentPassword;
	String newPassword;
	public PasswordChange() {
		super();
	}
	public PasswordChange(String currentPassword, String newPassword) {
		super();
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
