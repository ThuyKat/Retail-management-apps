package com.AllInSmall.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.AllInSmall.demo.model.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {

	private String name;
    private String description;
    private MultipartFile imageData; 
}
