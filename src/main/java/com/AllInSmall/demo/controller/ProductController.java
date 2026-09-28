package com.AllInSmall.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AllInSmall.demo.dto.ProductDTO;
import com.AllInSmall.demo.dto.ProductListWrapper;
import com.AllInSmall.demo.model.Category;
import com.AllInSmall.demo.model.Product;
import com.AllInSmall.demo.model.Size;
import com.AllInSmall.demo.repository.CategoryRepository;
import com.AllInSmall.demo.service.ProductService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
//@Tag(name = "Products", description = " Product management APIs")
@Hidden
public class ProductController {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductService productService;

//	@Operation(summary="Get form to add new product")
	@GetMapping("/product/form")
	public String showProductForm(Model model,
			@RequestParam(name = "showProducts", required = false, defaultValue = "false") boolean showProducts,
			@RequestParam(name = "categoryId", required = false) Integer categoryId,@RequestParam(name = "addSize", required = false, defaultValue = "false") boolean addSize,
			@RequestParam(name = "newProductId", required = false) Integer newProductId,HttpSession session) {
		// get category to render product form
		List<Category> category = categoryRepository.findAll();
		model.addAttribute("allCategory", category);
		
		if (newProductId != null) {
			model.addAttribute("newProductId", newProductId);
		}
		if (addSize && newProductId != null) {
			model.addAttribute("addSize", addSize);
			Product product = productService.findById(newProductId);
			model.addAttribute("priceNewProduct",product.getPrice());
		}
		// Add the product list to the model
		// default value: for cases when user directly accesses the POST URL without
		// using the form, and params is not included
		if (showProducts) {
			// only show category options that have products associated with it
			List<Category> allCategories = categoryRepository.findAll();
			List<Category> showCategories = new ArrayList<>();
			for(Category item : allCategories) {
				if(!item.getProducts().isEmpty()) {
					showCategories.add(item);
				}
			}
			model.addAttribute("categories", showCategories);
			// load product list
			List<Product> products = null;
			if (categoryId == null) {
				products = productService.findAllProducts();

			} else {
				products = productService.findProductByCategoryId(categoryId);
			}
			ProductListWrapper productListWrapper = new ProductListWrapper();
			productListWrapper.setProducts(products);
			model.addAttribute("productListWrapper", productListWrapper);
		}
		@SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		 session.setAttribute("navigationStack",navigationStack);

		// Set the showProducts attribute based on the request parameter
		model.addAttribute("showProducts", showProducts);

		return "addProductForm";
	}

	@PostMapping("/product/form")
	public String handleProductForm(RedirectAttributes redirectAttributes,
			@RequestParam(name = "showProducts", required = false, defaultValue = "false") boolean showProducts,HttpSession session) {
		// Redirect to the GET method with the showProducts parameter
		redirectAttributes.addAttribute("showProducts", showProducts);
		
		@SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		 session.setAttribute("navigationStack",navigationStack);
		return "redirect:/product/form";
	}
	
//	@Operation(summary="Add new product")
	@PostMapping("/product/add")
	public String addNewProduct(@RequestParam("productName") String name, @RequestParam("price") float price,
			@RequestParam("categoryId") Integer category_id, @RequestParam("imageData") MultipartFile file,RedirectAttributes redirectAttributes,HttpSession session) {
		try {
			Product product = productService.addNewProduct(name, price, category_id, file);
			redirectAttributes.addFlashAttribute("message","New product added successfully");
			redirectAttributes.addAttribute("newProductId", product.getId());
		} catch (Exception e) {
			
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error","Failed to add new product");

		}
		@SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		navigationStack.pop(); // pop the product detail page of deleted product
		 session.setAttribute("navigationStack",navigationStack);
		return "redirect:/product/form";

	}

//	@Operation(summary="Update all product prices")
	@PostMapping("product/updatePrice")
	public String updatePrices(@ModelAttribute("productList") ProductListWrapper productListWrapper,RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
		productService.updatePrices(productListWrapper);
		redirectAttributes.addFlashAttribute("message"," prices updated successfully");
		} catch (Exception e) {	
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error","Failed to update prices");
		}
		String referer = request.getHeader("referer");
		return "redirect:"+ referer;
	}

//	@Operation(summary="get all products")
	@GetMapping("product/currentProductList")
	public String showProductList(Model model, HttpServletRequest request,@RequestParam(name = "categoryId", required = false) Integer categoryId) {
		
		
		List<Product> products = null;
		if(categoryId == null) {
		 products =  productService.findAllProducts();
		}else {
			products = productService.findProductByCategoryId(categoryId);
		}
		ProductListWrapper productListWrapper = new ProductListWrapper();
		productListWrapper.setProducts(products);
		model.addAttribute("productListWrapper", productListWrapper);
		// all categories
		List<Category> allCategories = categoryRepository.findAll();
		List<Category> showCategories = new ArrayList<>();
		for(Category category : allCategories) {
			if(!category.getProducts().isEmpty()) {
				showCategories.add(category);
			}
		}
		model.addAttribute("categories", showCategories);
		// Set the current URI as a request attribute
		request.setAttribute("currentURI", request.getRequestURI());
		return "showProduct";
	}
	
	
	@GetMapping("/product/{id}")
	public String showProductDetails(@PathVariable Integer id, Model model,HttpSession session) {
		try {
			Product product = productService.getProductDetails(id);
			model.addAttribute("product", product);
			log.info("sizes of product:"+product.getSizes());
			@SuppressWarnings("unchecked")
			Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
			String page =navigationStack.pop();
			log.info("POP OUT OF NAV STACK: "+page);
			 session.setAttribute("navigationStack",navigationStack);
			return "showProductDetail";
		} catch (Exception e) {
			e.printStackTrace();
			
			model.addAttribute("message", "product not found");
			return "error";
		}

	}

	@PostMapping("/product/{id}")
	public String updateProductDetail(@ModelAttribute("product") ProductDTO productDTO, @PathVariable Integer id,
			@RequestParam(name = "imageData", required = false) MultipartFile image,
			RedirectAttributes redirectAttributes,HttpSession session) throws IOException {
		try {
			productService.updateProductDetail(productDTO, id, image);
			// Add a flash attribute for the success message
			redirectAttributes.addFlashAttribute("message", "Product updated successfully!");

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Failed to update product");
		}

		@SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		String page =navigationStack.pop();
		log.info("POP OUT OF NAV STACK: "+page);
		 session.setAttribute("navigationStack",navigationStack);
		// Redirect to the product details page
		return "redirect:/product/" + id;

	}
	

//	@Operation(summary="Safe delete product")
	@GetMapping("/product/delete")
	public String safeDeleteProduct(@RequestParam(name = "productId", required = true) Integer productId,
			RedirectAttributes redirectAttributes, HttpSession session) {

		try {
			productService.safeDeleteProduct(productId);
			
			redirectAttributes.addFlashAttribute("message", "Product deleted successfully");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to delete product");
		}
		@SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		 session.setAttribute("navigationStack",navigationStack);
		return "redirect:/product/currentProductList";
	}

}
