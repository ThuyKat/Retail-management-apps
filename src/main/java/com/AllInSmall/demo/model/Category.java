package com.AllInSmall.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
	
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String name;

@OneToMany(mappedBy = "category")
@JsonManagedReference
private List<Product>products;

@ManyToOne
@JsonBackReference
@JoinColumn(name = "parent_id")
private Category parent;

@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
@JsonManagedReference
private List<Category> subcategories = new ArrayList<>();

@Column(nullable = false)
private int level;

@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
private List<Size> sizes = new ArrayList<>();

}
