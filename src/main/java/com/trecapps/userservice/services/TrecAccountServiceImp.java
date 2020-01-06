package com.trecapps.userservice.services;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.trecapps.userservice.models.NewUser;
import com.trecapps.userservice.models.TrecAccount;
import com.trecapps.userservice.repositories.TrecAccountRepo;

@Service("TrecAccountService")
public class TrecAccountServiceImp implements TrecAccountService,  UserDetailsService {

	@Autowired
	TrecAccountRepo trecRepo;
	
	@Autowired
	TrecEmailService emailer;
	
	// chose a Character random from this String 
    final String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                + "0123456789"
                                + "abcdefghijklmnopqrstuvxyz";
    final int RANDOM_STRING_LENGTH = 20;
	
    @Autowired
	BCryptPasswordEncoder encoder;
	
	public TrecAccount getAccountById(long id)
	{	
		return trecRepo.getOne(id);
	}

	public TrecAccount getAccountByUserName(String username) 
	{
		return trecRepo.getTrecAccountByUsername(username);
	}

	public TrecAccount getAccountByEmail(String userEmail) 
	{
		return trecRepo.getTrecAccountByMainEmail(userEmail);
	}

	public TrecAccount updatePassword(long id, String oldPassword, String newPassword) 
	{
		TrecAccount account = trecRepo.getOne(id);
		if(account == null)
			return null;
		
		if(!encoder.matches(oldPassword, account.getToken()))
			return null;
		
		
		
		account.setToken(encoder.encode(newPassword));
		
		trecRepo.save(account);
		return account;
	}

	public TrecAccount createAccount(NewUser newUser) 
	{
		TrecAccount exists = trecRepo.getTrecAccountByUsername(newUser.getUsername());
		if(exists != null)
			return null;
		
		TrecAccount account = newUser.getTrecAccount();
		
		
		account.setToken(encoder.encode(account.getToken()));
		
		return trecRepo.save(account);
	}

	public TrecAccount logInUsername(String username, String password) 
	{
		TrecAccount exists = trecRepo.getTrecAccountByUsername(username);
		if(exists == null)
			return null;
		
		if(encoder.matches(password, exists.getToken()))
		{
			return exists;
		}
		
		return null;
	}

	public TrecAccount logInEmail(String username, String password) {
		TrecAccount exists = trecRepo.getTrecAccountByMainEmail(username);
		if(exists == null)
			return null;
		
		if(encoder.matches(password, exists.getToken()))
		{
			trecRepo.save(exists);
			return exists;
		}
		return null;
	}

	public boolean deleteAccount(long id, String password) {
		
		TrecAccount account = trecRepo.getOne(id);
		if(account == null)
			return false;
		
		if(!encoder.matches(password, account.getToken()))
			return false;
		trecRepo.delete(account);
		return true; 
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TrecAccount exists = trecRepo.getTrecAccountByUsername(username);
		if(exists == null)
			throw new UsernameNotFoundException("Could not find user by the name '" + username + "'");
		return exists;
	}

	public boolean verifyLoggedIn(long id, String username, String password) {
		TrecAccount ta = trecRepo.getOne(id);
		if(ta == null) return false;
		
		if(!encoder.matches(password, ta.getToken()))
			return false;
		
		trecRepo.save(ta);
		return true;
	}

	@Override
	public boolean verifyAccount(String username, String validationCode) {
		TrecAccount exists = trecRepo.getTrecAccountByUsername(username);
		if(exists == null)
		{
			throw new UsernameNotFoundException("Could not find user by the name '" + username + "'");
		}
		if(exists.getValidationToken().equals(validationCode))
		{
			exists.setIsValidated(1);
			trecRepo.save(exists);
			return true;
		}
		return false;
	}

	@Override
	public void sendverificationEmail(TrecAccount account) {
		String email = account.getMainEmail();
		String validationHash = generateRandomString();
		
		account.setValidationToken(validationHash);
		trecRepo.save(account);
		try {
			emailer.sendValidationEmail(email, "Trec-Account Verification", validationHash);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	private String generateRandomString()
	{
		StringBuilder sb = new StringBuilder();
		for(int c = 0; c < RANDOM_STRING_LENGTH; c++)
		{
			int ch = (int) (Math.random() * AlphaNumericString.length());
			sb.append(AlphaNumericString.charAt(ch));
		}
		return sb.toString();
	}

	@Override
	public boolean updateUser(TrecAccount account) {
		TrecAccount token = trecRepo.getOne(account.getAccountId());
		if(token == null)
			return false;
		account.setToken(token.getToken());
		return trecRepo.save(account) != null;
	}


}
