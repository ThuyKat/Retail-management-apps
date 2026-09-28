package com.AllInSmall.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AllInSmall.demo.model.Category;
import com.AllInSmall.demo.model.Size;
import com.AllInSmall.demo.repository.CategoryRepository;
import com.AllInSmall.demo.repository.ProductRepository;
import com.AllInSmall.demo.repository.SizeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	@Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    public Category addCategory(String name, Integer parentId) {
    	 // Check if a category with the same name already exists (case-insensitive)
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("A category with this name already exists");
        }
        Category category = new Category();
        category.setName(name);

        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
            category.setParent(parent);
            category.setLevel(parent.getLevel() + 1);
        } else {
            category.setLevel(0);
        }

        return categoryRepository.save(category);
    }
    
 
    

    public List<Category> getTopLevelCategories() {
        return categoryRepository.findByParentIsNull();
    }
//
//    public List<Category> getSubcategories(int parentId) {
//        return categoryRepository.findByParentId(parentId);
//    }

   

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}
    
	public Category getCategoryById(Integer categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId)); 
	}

	public void updateCategory(Category category) {
		 categoryRepository.save(category);
		
	}

	@Transactional
	public void deleteCategory(Integer id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
		if(categoryHasProducts(category)) {
			throw new IllegalStateException("Cannot delete category.It or its subcategories have aossicated products");
		}
		
		deleteCategoryRecursively(category);
	
	}

	private boolean categoryHasProducts(Category category) {
		//check if the current category has products
		if(!productRepository.findProductByCategoryId(category.getId()).isEmpty()) {
			return true;
		}
		
		//recursively check subCategories
		for (Category subCategory : category.getSubcategories()) {
			if(categoryHasProducts(subCategory)) {
				return true;
			}
		}
		return false;
	}

	private void deleteCategoryRecursively(Category category) {
		// recursively delete all subCategories
		for(Category subCategory : new ArrayList<>(category.getSubcategories())) { // this is to avoid iterating over a collection and modifying it at the same time -> ConcurrentModificationException
			deleteCategoryRecursively(subCategory);
		}
		//Finally delete the category itself
		categoryRepository.delete(category);
		
	}

	
}
