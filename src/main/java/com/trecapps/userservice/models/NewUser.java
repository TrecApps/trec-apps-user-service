package com.trecapps.userservice.models;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class NewUser {

	String firstName;
	String lastName;
	String username;
	String mainEmail;
	String trecEmail;
	String backupEmail;
	String password;
	Date birthday;
	
	public TrecAccount getTrecAccount()
	{
		return new TrecAccount(0L, firstName, lastName, username, mainEmail, trecEmail,
			backupEmail, password, birthday, 0, null, null);
	}
	
	public NewUser(String firstName, String lastName, String username, String mainEmail, String trecEmail,
			String backupEmail, String password, Date birthday) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.mainEmail = mainEmail;
		this.trecEmail = trecEmail;
		this.backupEmail = backupEmail;
		this.password = password;
		this.birthday = birthday;
	}
	public NewUser() {
		super();
	}
	@Override
	public String toString() {
		return "NewUser [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username + ", mainEmail="
				+ mainEmail + ", trecEmail=" + trecEmail + ", backupEmail=" + backupEmail + ", Password=" + password
				+ ", birthday=" + birthday + "]";
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMainEmail() {
		return mainEmail;
	}
	public void setMainEmail(String mainEmail) {
		this.mainEmail = mainEmail;
	}
	public String getTrecEmail() {
		return trecEmail;
	}
	public void setTrecEmail(String trecEmail) {
		this.trecEmail = trecEmail;
	}
	public String getBackupEmail() {
		return backupEmail;
	}
	public void setBackupEmail(String backupEmail) {
		this.backupEmail = backupEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	
}
