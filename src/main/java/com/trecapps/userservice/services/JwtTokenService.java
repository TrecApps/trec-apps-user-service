package com.trecapps.userservice.services;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.trecapps.userservice.models.primary.TrecAccount;

@Service
public class JwtTokenService {
	
	@Value("trec.key.public")
	String publicKeyStr;
	
	@Value("trec.key.private")
	String privateKeyStr;
	
	RSAPublicKey publicKey;
	
	RSAPrivateKey privateKey;
	
	@Autowired
	TrecAccountService accountService;
	
	private boolean setKeys()
	{
		if(publicKey == null)
		{
			try {
				publicKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(
						new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr)));
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
		
		if(privateKey == null)
		{
			try
			{
				privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(
						new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr)));	
			}catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		return privateKey != null && publicKey != null;
	}

	public String generateToken(TrecAccount account)
	{
		if(account == null)
			return null;
		
		if(!setKeys())
			return null;
		
		privateKey.getAlgorithm();
		
		return JWT.create().withIssuer("Trec-Apps-User-Service")
		.withClaim("Username", account.getUsername())
		.withClaim("FirstName", account.getFirstName())
		.withClaim("LastName", account.getLastName())
		.withClaim("ID", account.getAccountId())
		.withClaim("Validated", account.getIsValidated() != 0)
		.withClaim("Color", account.getColor())
		.withClaim("MainEmail", account.getMainEmail())
		.withClaim("TrecEmail", account.getTrecEmail())
		.withClaim("BackupEmail", account.getBackupEmail())
		.withClaim("Birthday", account.getBirthday())
		.sign(Algorithm.RSA512(publicKey, privateKey));
		

	}
	
	public TrecAccount verifyToken(String token)
	{
		DecodedJWT decodedJwt = null;
		try
		{
			decodedJwt = JWT.require(Algorithm.RSA512(publicKey,privateKey))
					.build()
					.verify(token);
		}
		catch(JWTVerificationException e)
		{
			e.printStackTrace();
			return null;
		}
		
		Claim idClaim = decodedJwt.getClaim("ID");
		
		Long idLong = idClaim.asLong();
		
		if(idLong == null)
			return null;
		
		
		
		return accountService.getAccountById(idLong);
	}
}
