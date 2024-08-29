package com.AllInSmall.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AllInSmall.demo.dto.ProductListWrapper;
import com.AllInSmall.demo.model.Category;
import com.AllInSmall.demo.model.Product;
import com.AllInSmall.demo.repository.CategoryRepository;
import com.AllInSmall.demo.repository.ProductRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/product/form")
    public String showProductForm(Model model, @RequestParam(name = "showProducts", required = false, defaultValue = "false") boolean showProducts) {
	    //get category to render product form  
		List<Category> category = categoryRepository.findAll();
		model.addAttribute("allCategory", category);
		// Add the product list to the model
		//default value: for cases when user directly accesses the POST URL without using the form, and params is not included
		if (showProducts) {
		List<Product> products = productRepository.findAll();
		ProductListWrapper productListWrapper = new ProductListWrapper();
		productListWrapper.setProducts(products);
		model.addAttribute("productListWrapper", productListWrapper);
		}

		// Set the showProducts attribute based on the request parameter
        model.addAttribute("showProducts", showProducts);
		
				
		return "addProductForm";
	}
	
	@PostMapping("/product/form")
    public String handleProductForm(RedirectAttributes redirectAttributes, @RequestParam(name = "showProducts", required = false, defaultValue = "false") boolean showProducts) {
        // Redirect to the GET method with the showProducts parameter
        redirectAttributes.addAttribute("showProducts", showProducts);
        return "redirect:/product/form";
    }
    
	@PostMapping("/product/add")
	public String addNewProduct(@RequestParam("productName") String name, @RequestParam("price") float price,
			@RequestParam("categoryId") Integer category_id, @RequestParam("imageData") MultipartFile file) {
		try {
			Product product = new Product();
			product.setName(name);
			product.setPrice(price);
			product.setImageData(file.getBytes());
			product.setImageName(file.getOriginalFilename());
			product.setCreatedBy("Thuy");
			Category category = categoryRepository.findById(category_id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id"));
			product.setCategory(category);
			productRepository.save(product);
			log.info("NEW PRODUCT SAVED TO DATABASE: "+product.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/product/form";

	}
	
	 @PostMapping("product/updatePrice")
	    public String updatePrices(@ModelAttribute("productList") ProductListWrapper productListWrapper, Model model) {
		 List<Product>products = productListWrapper.getProducts();
		 for(Product product : products) {
			 Optional<Product> productOptional = productRepository.findById(product.getId());
			 if (productOptional.isPresent()){
				 Product productDB = productOptional.get();
				 productDB.setPrice(product.getPrice());
				 productRepository.save(productDB); // save will update the existing record if ID is not null 
			 }
		 }
	        return "redirect:/product/form"; // Redirect to the product list page
	    }
	 
	 @GetMapping("product/currentProductList")
	 public String showProductList(Model model, HttpServletRequest request) {
		 List<Product> products = productRepository.findAll();
			ProductListWrapper productListWrapper = new ProductListWrapper();
			productListWrapper.setProducts(products);
			model.addAttribute("productListWrapper", productListWrapper);
			// Set the current URI as a request attribute
			request.setAttribute("currentURI", request.getRequestURI());
		 return "showProduct";
	 }
	 
	 
	}

