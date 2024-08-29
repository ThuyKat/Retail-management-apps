package com.AllInSmall.demo.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "persistent_logins")
public class PersistentLogin {
	@Id
    private String series;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String token;
    
    @Column(name = "last_used", nullable = false)
    private Date lastUsed;

}
