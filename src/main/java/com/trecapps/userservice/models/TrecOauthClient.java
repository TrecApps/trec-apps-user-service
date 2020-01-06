package com.trecapps.userservice.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;

import com.trecapps.userservice.security.TrecAuthority;

@Component
@Table
@Entity
public class TrecOauthClient implements ClientDetails{

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -4948008937850771576L;

	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	TrecAccount owner;
	
	public TrecAccount getOwner()
	{
		return owner;
	}
	
	public void setOwner(TrecAccount owner)
	{
		this.owner = owner;
	}
	
	@Column
	String name;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public TrecOauthClient() {
		super();
		this.clientType = 0;
	}

	public TrecOauthClient(String name, TrecAccount owner, String clientId, String resourceIds, int clientType, String clientSecret) {
		super();
		this.name = name;
		this.owner = owner;
		this.clientId = clientId;
		this.resourceIds = resourceIds;
		this.clientType = clientType;
		this.clientSecret = clientSecret;
	}

	@Id
	String clientId;
	
	@Override
	public String getClientId() {
		// TODO Auto-generated method stub
		return clientId;
	}
	
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	
	@Column
	String resourceIds;

	@Override
	public Set<String> getResourceIds() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transient
	public static final int CLIENT_TYPE_WEB_SERVICE = 0;
	@Transient
	public static final int CLIENT_TYPE_SINGLE_PAGE = 1;
	@Transient
	public static final int CLIENT_TYPE_NATIVE_APP = 2;
	@Transient
	public static final int CLIENT_TYPE_MOBILE_APP = 3;
	
	@Column
	int clientType;
	
	public int getClientType()
	{
		return clientType;
	}
	
	public void setClientType(int clientType)
	{
		if(clientType >= 0 && clientType < 5)
			this.clientType = clientType;
		else
			this.clientType = 0;
	}
	
	@Column
	String clientSecret;

	@Override
	public boolean isSecretRequired() {
		// TODO Auto-generated method stub
		return clientType == 0;
	}

	@Override
	public String getClientSecret() {
		if(isSecretRequired()) return clientSecret;
		
		return null;
	}
	
	public void setClientSecret(String clientSecret)
	{
		this.clientSecret = clientSecret;
	}

	@Override
	public boolean isScoped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> ret = new ArrayList<>();
		
		ret.add(new TrecAuthority("CLIENT"));
		
		return ret;
	}

	@Override
	public Integer getAccessTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAutoApprove(String scope) {		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "TrecOauthClient [owner=" + owner + ", name=" + name + ", clientId=" + clientId + ", resourceIds="
				+ resourceIds + ", clientType=" + clientType + ", clientSecret=" + clientSecret + "]";
	}
	
	
	

}
