package com.AllInSmall.demo.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.AllInSmall.demo.dto.MyUserDetails;
import com.AllInSmall.demo.model.Role;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.repository.RoleRepository;
import com.AllInSmall.demo.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private RoleHierarchy roleHierarchy;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

		Set<GrantedAuthority> authorities = new HashSet<>(user.getRole().getAuthorities());
		
		// Use role hierarchy to determine all applicable roles
        Collection<? extends GrantedAuthority> hierarchicalAuthorities = 
            roleHierarchy.getReachableGrantedAuthorities(authorities);
        
        // Include all initial and hierarchical authorities
        Set<GrantedAuthority> allAuthorities = new HashSet<>(authorities);
        allAuthorities.addAll(hierarchicalAuthorities);
        
     // Add authorities for roles that weren't in the original set
        for (GrantedAuthority authority : hierarchicalAuthorities) {
            if (authority.getAuthority().startsWith("ROLE_") && !authorities.contains(authority)) {
                String roleName = authority.getAuthority().substring(5);
                allAuthorities.addAll(getAuthoritiesForRole(roleName));
            }
        }
       
		return new MyUserDetails(user.getUsername(),user.getPassword(),user.getStatus(),allAuthorities, user.getRole());

	}

	private Collection<? extends GrantedAuthority> getAuthoritiesForRole(String roleName) {
		List<Role>roles = roleRepository.findAll();
		
		for(Role role : roles) {
			if(roleName.equalsIgnoreCase(role.getRoleName())) {
				return role.getAuthorities();
			}
		}
		return Collections.emptyList();
	}

}
