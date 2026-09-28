package com.AllInSmall.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.*;

//import lombok.ToString;

@Data // this to generate getter, setter, toString and hashcode, equals
@AllArgsConstructor
@NoArgsConstructor
@Entity // for mapping to db
@Table(name = "permissions")
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(name = "permission_name")
	String permissionName;

	@ManyToMany(mappedBy = "permissions")
//	@JoinColumn(name = "role_id")
	@JsonIgnore
	private List<Role> roles;
	
	@Override
    public String toString() {
        return "Permission{" +
                "name='" + permissionName + '\'' +
                // Avoid including the role to prevent circular reference
                '}';
    }

}
