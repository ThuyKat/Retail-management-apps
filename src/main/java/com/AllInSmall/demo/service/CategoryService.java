package com.AllInSmall.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AllInSmall.demo.model.Category;
import com.AllInSmall.demo.model.Size;
import com.AllInSmall.demo.repository.CategoryRepository;
import com.AllInSmall.demo.repository.SizeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SizeRepository sizeRepository;
    
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
    
    public Size addSize(String name, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Size size = new Size();
        size.setName(name);
        size.setCategory(category);

        return sizeRepository.save(size);
    }
    

    public List<Category> getTopLevelCategories() {
        return categoryRepository.findByParentIsNull();
    }
//
//    public List<Category> getSubcategories(int parentId) {
//        return categoryRepository.findByParentId(parentId);
//    }

    public List<Size> getSizes(int categoryId) {
        return sizeRepository.findByCategoryId(categoryId);
    }

	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}
    
	public Category getCategoryById(Integer categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId)); 
	}

	public void updateCategory(Category category) {
		 categoryRepository.save(category);
		
	}

	public Size getSizeById(int sizeId) {
		// TODO Auto-generated method stub
		return sizeRepository.findById(sizeId).orElseThrow(() -> new EntityNotFoundException("Size not found with id: " + sizeId));
	}

	public void updateSize(Size size) {
		sizeRepository.save(size);
		
	}
}
