package com.AllInSmall.demo.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="role_name")
	private String roleName;
	
	@ManyToMany(fetch=FetchType.EAGER,cascade= CascadeType.ALL)
	@JoinTable(
		name = "permission_role",
		joinColumns = @JoinColumn(name = "role_id"),
		inverseJoinColumns = @JoinColumn(name = "permission_id")
			) // once role saved,  CascadeType.ALL will save permissions into permissions table and populates role_permission
	//by default, when no cascadeType applies, only roles and permission_role tables are updated
	private Set<Permission> permissions;
	
	@OneToMany(mappedBy ="role")
//	@JsonManagedReference
	@JsonIgnore
	private List<User> users;
	
	  @Override
	    public String toString() {
	        return "Role{" +
	                "name='" + roleName + '\'' +
	                ", permissionsCount=" + (permissions != null ? permissions.size() : 0) + // Avoid full list
	                '}';
	    }
	  
	  public List<GrantedAuthority> getAuthorities(){
		  List<GrantedAuthority>authorities = getPermissions()
		  .stream()
		  .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
		  .collect(Collectors.toList());
		  
		  authorities.add(new SimpleGrantedAuthority("ROLE_"+this.roleName));
		  return authorities;
	  }
	}

