package com.trecapps.userservice.models.primary;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

// import com.trecapps.userservice.security.TrecAuthority;

@Component
@Table
@Entity
public class TrecAccount // implements UserDetails
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1456689615303218227L;
	String firstName;
	String lastName;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TrecSequence")
	@SequenceGenerator(name="TrecSequence", allocationSize=1, sequenceName="SQ_TREC_PK")
	@NotNull
	long accountId;
	
	@Column
	@Size(min=6, max = 30)
	@NotNull
	String username;
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	String mainEmail;
	String trecEmail;
	String backupEmail;
	String token;
	Date birthday;
	int isValidated;
	String color;
	String validationToken;
	
	public TrecAccount(long accountId, String firstName, String lastName, String username, String mainEmail, String trecEmail,
			String backupEmail, String token, Date birthday, int isValidated, String color, String validationToken) {
		super();
		this.accountId = accountId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.mainEmail = mainEmail;
		this.trecEmail = trecEmail;
		this.backupEmail = backupEmail;
		this.token = token;
		this.birthday = birthday;
		this.isValidated = isValidated;
		this.color = color;
		this.validationToken = validationToken;
	}
	public TrecAccount() {
		super();
	}
	@Override
	public String toString() {
		return "TrecAccount [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", mainEmail=" + mainEmail + ", trecEmail=" + trecEmail + ", backupEmail=" + backupEmail + ", token="
				+ token + ", birthday=" + birthday + ", isValidated=" + isValidated + ", color=" + color + "]";
	}
	
	
	
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getIsValidated() {
		return isValidated;
	}
	public void setIsValidated(int isValidated) {
		this.isValidated = isValidated;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getValidationToken() {
		return validationToken;
	}
	public void setValidationToken(String validationToken) {
		this.validationToken = validationToken;
	}
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		List<TrecAuthority> ret = new ArrayList<>();
//		ret.add(new TrecAuthority("USER"));
//		return ret;
//	}
//	@Override
//	public String getPassword() {
//		return token;
//	}
//	@Override
//	public boolean isAccountNonExpired() {
//		return false;
//	}
//	@Override
//	public boolean isAccountNonLocked() {
//		return false;
//	}
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return false;
//	}
//	@Override
//	public boolean isEnabled() {
//		return isValidated != 0;
//	}
	
	
}
