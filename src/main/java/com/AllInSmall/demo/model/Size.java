package com.AllInSmall.demo.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sizes")
public class Size {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private Product product;
    
    @OneToMany(mappedBy ="size")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
    
    @Column(name="size_price")
    private Double sizePrice;
    
    public Double getPrice() {
        return sizePrice != null ? sizePrice : product.getPrice();
    }
}
