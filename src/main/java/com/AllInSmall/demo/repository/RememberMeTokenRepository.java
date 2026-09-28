package com.AllInSmall.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.model.PersistentLogin;

@Repository
public interface RememberMeTokenRepository extends JpaRepository<PersistentLogin, String> {

	
	 void deleteByUsername(String username);
}
