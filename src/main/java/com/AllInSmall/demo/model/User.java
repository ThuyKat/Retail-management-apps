package com.AllInSmall.demo.model;

import java.util.List;
import java.util.Set;

import com.AllInSmall.demo.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String username;
	
	private String password;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	private String mobile;
	
	private String email;
	
	@OneToMany(mappedBy = "user")
	@JsonManagedReference
	private List<Order> orders;
	
	@OneToMany(mappedBy = "user")
	@JsonManagedReference
	private Set<Comment>comments;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
//	@JsonBackReference
	@JoinColumn(name = "role_id")
	private Role role;
	
	private UserStatus status;
	
	@OneToOne(mappedBy = "user")
	private VerificationToken token;
	
	@Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", tokensCount=" + (token != null ? 1 : 0) + // Avoid full list
                '}';
    }
}
