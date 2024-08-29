package com.AllInSmall.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AllInSmall.demo.model.Category;
import com.AllInSmall.demo.model.Size;
import com.AllInSmall.demo.service.CategoryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public String viewCategory(Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		List<Category> topLevelCategories = categoryService.getTopLevelCategories();
		model.addAttribute("topLevelCategories", topLevelCategories);
		return "viewCategory";
	}

	@GetMapping("/form")
	public String showAddCategoryForm(Model model,
			@RequestParam(name = "addSize", required = false, defaultValue = "false") boolean addSize,
			@RequestParam(name = "newCategoryId", required = false) Integer newCategoryId) {
		model.addAttribute("categories", categoryService.getAllCategories());

		if (newCategoryId != null) {
			model.addAttribute("newCategoryId", newCategoryId);
		}
		if (addSize && newCategoryId != null) {
			model.addAttribute("addSize", addSize);
		}

		List<Category> topLevelCategories = categoryService.getTopLevelCategories();
		model.addAttribute("topLevelCategories", topLevelCategories);

		return "addCategory";
	}

	@PostMapping
	public String addCategory(@RequestParam(required = true, name = "name") String name,
			@RequestParam(required = false, name = "parentId") Integer parentId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Category category = categoryService.addCategory(name, parentId);
			redirectAttributes.addFlashAttribute("message", "Category added successfully");
			redirectAttributes.addAttribute("newCategoryId", category.getId());

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error adding category: " + e.getMessage());
		}
		return "redirect:/category/form";
	}

	@PostMapping("/{categoryId}/size")
	public String addSize(@PathVariable int categoryId, @RequestParam(name = "name") String name, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			Size size = categoryService.addSize(name, categoryId);
			Category category = categoryService.getCategoryById(categoryId);

			redirectAttributes.addFlashAttribute("message",
					"new sized " + size.getName() + " is saved for category :" + category.getName());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error adding size: " + e.getMessage());
		}
		redirectAttributes.addAttribute("newCategoryId", categoryId);
		redirectAttributes.addAttribute("addSize", true);
		String refererURL = request.getHeader("Referer");
		return "redirect:"+refererURL;
	}
	
	@GetMapping("/edit/{categoryId}")
	public String editCategory(@PathVariable int categoryId,Model model) {
		Category category = categoryService.getCategoryById(categoryId);
		List<Category>allCategories = categoryService.getAllCategories();
		model.addAttribute("category", category);
		model.addAttribute("allCategories", allCategories);
		return "editCategory";
	}
	
	@GetMapping("/size/edit/{sizeId}")
	public String editSize(@PathVariable int sizeId,Model model) {
		Size size = categoryService.getSizeById(sizeId);
		model.addAttribute("size", size);
		return "editSize";
	}
	
	@PostMapping("/size/update")
	public String updateSize(@RequestParam(required=true,name="name")String sizeName, @RequestParam(required=true,name="id") Integer sizeId,RedirectAttributes redirectAttributes) {
		try {
			Size size = categoryService.getSizeById(sizeId);
			if(size ==null) {
				 throw new EntityNotFoundException("Size not found with id: " + sizeId);
			}
			size.setName(sizeName);
			categoryService.updateSize(size);

	        redirectAttributes.addFlashAttribute("message", "Size is updated successfully");
	    } catch (EntityNotFoundException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error updating category: " + e.getMessage());
	    }

	    return "redirect:/category";
		
	}
	
	@PostMapping("/update")
	public String updateCategory(@RequestParam(required = true, name = "id") Integer categoryId,
	                             @RequestParam(required = true, name = "name") String categoryName,
	                             @RequestParam(required = false, name = "parentId") Integer parentId,
	                             RedirectAttributes redirectAttributes) {
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

	    return "redirect:/category";
	}
	
	

}
