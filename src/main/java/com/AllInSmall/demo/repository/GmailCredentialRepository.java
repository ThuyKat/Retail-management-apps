package com.AllInSmall.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AllInSmall.demo.model.GmailServiceCredential;

public interface GmailCredentialRepository extends JpaRepository<GmailServiceCredential,Integer> {

}
