package com.trecapps.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trecapps.userservice.models.TrecOauthClient;

@Repository
public interface TrecOauthClientRepo extends JpaRepository<TrecOauthClient, String> {

	@Query("select t from TrecOauthClient t where t.clientId = ?1")
	TrecOauthClient getTrecOauthClientByClientId(String clientId);
}
