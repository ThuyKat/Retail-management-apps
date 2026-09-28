package com.AllInSmall.demo.repository;
//import java.util.Optional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.AllInSmall.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

	public User getUserByEmailAndPassword(String email,String password);

	public Optional<User> findByUsername(String username);

	public User findByEmail(String email);
}
