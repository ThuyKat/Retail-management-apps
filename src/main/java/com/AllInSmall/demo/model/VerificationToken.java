package com.AllInSmall.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String token;
	
	@OneToOne(targetEntity = User.class,fetch=FetchType.EAGER)
	@JoinColumn(nullable = false,name="user_id")
	private User user;
	
	private LocalDateTime expiryDate;	
	
	public VerificationToken(String token, User user) {
		this.token = token;
		this.user =user;
		this.expiryDate = LocalDateTime.now().plusHours(24); //token expires in 24 hours
	}
	 @Override
	    public String toString() {
	        return "VerificationToken{" +
	                "token='" + token + '\'' +
	                // Avoid including the user to prevent circular reference
	                '}';
	    }
}
