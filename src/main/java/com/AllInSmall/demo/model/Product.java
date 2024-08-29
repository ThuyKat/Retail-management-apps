package com.AllInSmall.demo.model;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // this to generate getter, setter, toString and hashcode, equals
@AllArgsConstructor
@NoArgsConstructor
@Entity // for mapping to db 
@ToString(exclude= {"category","orderDetails"})
@Table(name="products")
public class Product {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

@Column(name="image_name")
private String imageName;

@Lob
@Column(name="image_data",columnDefinition="LONGBLOB")
private byte[] imageData;

@Transient
public String getBase64Image() {
	if(imageData != null && imageData.length > 0) {
		return Base64.getEncoder().encodeToString(imageData);
	}
	return null;
}

@Column(name="created_by")	
private String createdBy;

@Column(name="created_date")
private LocalDateTime createdDate;

@Column(name="modify_by")
private String modifiedBy;

@Column(name="modify_date")
private LocalDateTime modifiedDate;



@ManyToOne
@JsonBackReference
@JoinColumn(name = "category_id")
private Category category;


private String name;


private float price;

@Column(columnDefinition = "TEXT")
private String description;

@OneToMany(mappedBy = "product") // name of object product in OrderDetail
@JsonManagedReference
private List<OrderDetail>orderDetails;

@PrePersist
protected void onCreate() {
	createdDate = LocalDateTime.now();
}

@PreUpdate 
protected void onUpdate() {
	modifiedDate = LocalDateTime.now();
}

	


}
