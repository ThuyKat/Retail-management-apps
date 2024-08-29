package com.AllInSmall.demo.dto;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.AllInSmall.demo.enums.UserStatus;
import com.AllInSmall.demo.model.Role;
import com.AllInSmall.demo.model.User;


public class MyUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private UserStatus status;
	private Set<GrantedAuthority> authorities;
	private Role role;
	
//	public MyUserDetails(User user) {
//		this.userName = user.getUsername();
//		this.password = user.getPassword();
//		this.setActive((user.getStatus() == UserStatus.ACTIVE)) ;
//		this.role = user.getRole();
//		this.authorities = user.getRole().getAuthorities();
//		
//	}
	
	
	
	public MyUserDetails(String userName, String password, UserStatus status, Set<GrantedAuthority> authorities,
			Role role) {
		super();
		this.userName = userName;
		this.password = password;
		this.setStatus(status);
		this.authorities = authorities;
		this.role = role;
	}





	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password ;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}



	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}



	public UserStatus getStatus() {
		return status;
	}



	public void setStatus(UserStatus status) {
		this.status = status;
	}

}
