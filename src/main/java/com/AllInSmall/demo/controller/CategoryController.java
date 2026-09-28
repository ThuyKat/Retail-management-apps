package com.AllInSmall.demo.controller;

import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AllInSmall.demo.model.Category;
import com.AllInSmall.demo.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/category")
@Tag(name = "Categories", description = "Category management APIs")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Operation(summary = "View all categories")
	@GetMapping
	public String viewCategory(Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		List<Category> topLevelCategories = categoryService.getTopLevelCategories();
		model.addAttribute("topLevelCategories", topLevelCategories);
		return "viewCategory";
	}

	@Operation(summary = "View form to add new category")
	@GetMapping("/form")
	public String showAddCategoryForm(Model model,HttpSession session) {
		model.addAttribute("categories", categoryService.getAllCategories());

		List<Category> topLevelCategories = categoryService.getTopLevelCategories();
		model.addAttribute("topLevelCategories", topLevelCategories);
		@SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		 session.setAttribute("navigationStack",navigationStack);
		return "addCategory";
		
	}

	@Operation(summary = "Add new category")
	@PostMapping
	public String addCategory(@RequestParam(required = true, name = "name") String name,
			@RequestParam(required = false, name = "parentId") Integer parentId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.addCategory(name, parentId);
			redirectAttributes.addFlashAttribute("message", "Category added successfully");
			

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error adding category: " + e.getMessage());
		}
		return "redirect:/category/form";
	}


	@Operation(summary = "Get category by ID")
	@GetMapping("/edit/{categoryId}")
	public String editCategory(@PathVariable int categoryId,Model model,HttpSession session) {
		Category category = categoryService.getCategoryById(categoryId);
		List<Category>allCategories = categoryService.getAllCategories();
		model.addAttribute("category", category);
		model.addAttribute("allCategories", allCategories);
		@SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		 session.setAttribute("navigationStack",navigationStack);
		return "editCategory";
	}
	
	@Operation(summary = "Edit category")
	@PostMapping("/update")
	public String updateCategory(@RequestParam(required = true, name = "id") Integer categoryId,
	                             @RequestParam(required = true, name = "name") String categoryName,
	                             @RequestParam(required = false, name = "parentId") Integer parentId,
	                             RedirectAttributes redirectAttributes, HttpSession session) {
	    try {
	        Category category = categoryService.getCategoryById(categoryId);
	        if (category == null) {
	            throw new EntityNotFoundException("Category not found with id: " + categoryId);
	        }

	        // Update category name
	        category.setName(categoryName);

	        // Update parent category if parentId is provided
	        if (parentId != null) {
	            Category parentCategory = categoryService.getCategoryById(parentId);
	            if (parentCategory == null) {
	                throw new EntityNotFoundException("Parent category not found with id: " + parentId);
	            }
	            category.setParent(parentCategory);
	        } else {
	            // If parentId is null, set as top-level category
	            category.setParent(null);
	        }

	        // Save the updated category
	        categoryService.updateCategory(category);
	        
	        redirectAttributes.addFlashAttribute("message", "Category updated successfully");
	    } catch (EntityNotFoundException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error updating category: " + e.getMessage());
	    }

	    @SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		 session.setAttribute("navigationStack",navigationStack);
	    return "redirect:/category";
	}
	
	@Operation(summary = "Get category by ID")
	@GetMapping("/delete/{id}")
	public String deleteCategory (@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
		try {
			categoryService.deleteCategory(id);
			
			redirectAttributes.addFlashAttribute("message","Category deleted successfully");
			
		}catch(Exception e) {
			redirectAttributes.addFlashAttribute("error","Failed to delete category");
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		 session.setAttribute("navigationStack",navigationStack);
		return "redirect:/category";
	}
	
	
	

}
