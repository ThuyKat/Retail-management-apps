package com.AllInSmall.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.model.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

	VerificationToken findByToken(String token);

	VerificationToken findByUserId(int id);

}
