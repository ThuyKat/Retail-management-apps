package com.AllInSmall.demo.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AllInSmall.demo.dto.ProductDTO;
import com.AllInSmall.demo.model.Product;
import com.AllInSmall.demo.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProductDetailController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/product/details")
	public String showProductDetails(@RequestParam(name = "productId") int productId, Model model) {
		Optional<Product> productOptional = productRepository.findById(productId);
		 if(productOptional.isPresent()) {
			 Product product = productOptional.get();
			 model.addAttribute("product", product);
		 }
		
		
		return "showProductDetail";
		
	}

		@PostMapping("/product/details")
		 public String updateProductDetail(@ModelAttribute("product") ProductDTO productDTO, @RequestParam(name ="productId",required=true) int productId, @RequestParam("imageData") MultipartFile image, RedirectAttributes redirectAttributes) throws IOException {
			 String productName = productDTO.getName();
			 String productDescription = productDTO.getDescription();
			 MultipartFile productImageData = productDTO.getImageData();
			 // find product by ID from database
			
			 Optional<Product> productOptional = productRepository.findById(productId);
			 if(productOptional.isPresent()) {
				 Product productDB = productOptional.get();
				 productDB.setName(productName);
				 productDB.setDescription(productDescription);
				 if (productDB.getDescription().length() > 65535) { // Adjust based on the column type
					    throw new IllegalArgumentException("Description is too long.");
					}
				 if (image !=null && !image.isEmpty()) {
					 productDB.setImageData(productImageData.getBytes());
					 productDB.setImageName(productImageData.getOriginalFilename());
				 }
				 productDB.setModifiedBy("Thuy");
				 productRepository.save(productDB);
				 // Add a flash attribute for the success message
		         redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
				
			 }
			 // Redirect to the product details page
			        return "redirect:/product/details?productId=" + productId;
			 
		 }
}
