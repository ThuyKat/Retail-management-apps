package com.AllInSmall.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import com.AllInSmall.demo.dto.ProductDTO;
import com.AllInSmall.demo.dto.ProductListWrapper;
import com.AllInSmall.demo.model.Category;
import com.AllInSmall.demo.model.OrderDetail;
import com.AllInSmall.demo.model.Product;
import com.AllInSmall.demo.model.Size;
import com.AllInSmall.demo.repository.CategoryRepository;
import com.AllInSmall.demo.repository.OrderDetailRepository;
import com.AllInSmall.demo.repository.ProductRepository;
import com.AllInSmall.demo.repository.SizeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SizeRepository sizeRepository;

	public void safeDeleteProduct(Integer productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));

		// check if product has been ordered previously
		if (productHasOrderDetails(product)) {
			throw new IllegalStateException("Cannot delete product.It has associated orders");
		}

		// Delete all sizes associated with this product
		for (Size size : new ArrayList<>(product.getSizes())) {
			sizeRepository.delete(size);
		}
		productRepository.delete(product);
	}

	private boolean productHasOrderDetails(Product product) {
		if (!orderDetailRepository.findByProductId(product.getId()).isEmpty()) {
			return true;
		}
		return false;
	}

	public Product addNewProduct(String name, float price, Integer category_id, MultipartFile file) {
		Product savedProduct = null;
		
		try {

			Product product = new Product();
			product.setName(name);
			product.setPrice(price);
			product.setImageData(file.getBytes());
			product.setImageName(file.getOriginalFilename());
			product.setCreatedBy("Owner");
			Category category = categoryRepository.findById(category_id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid category Id"));
			product.setCategory(category);
			savedProduct = productRepository.save(product);
			log.info("NEW PRODUCT SAVED TO DATABASE: " + product.getName());
		} catch (IOException e) {
			e.printStackTrace();
			log.info("UNABLE TO SAVE PRODUCT TO DATABASE");
		}
		return savedProduct;

	}

	@Transactional
	public void updatePrices(ProductListWrapper productListWrapper) {
		List<Product> products = productListWrapper.getProducts();
		for (Product product : products) {
			Optional<Product> productOptional = productRepository.findById(product.getId());
			if (productOptional.isPresent()) {
				Product productDB = productOptional.get();
				productDB.setPrice(product.getPrice());
				productRepository.save(productDB); // save will update the existing record if ID is not null
			}
		}
	}

	@Transactional
	public void updateProductDetail(ProductDTO productDTO, Integer productId, MultipartFile image) throws IOException {

		String productName = productDTO.getName();
		String productDescription = productDTO.getDescription();
		MultipartFile productImageData = productDTO.getImageData();

		// find product by ID from database

		Optional<Product> productOptional = productRepository.findById(productId);
		if (productOptional.isPresent()) {
			Product productDB = productOptional.get();
			productDB.setName(productName);
			productDB.setDescription(productDescription);
			if (productDB.getDescription().length() > 65535) { // Adjust based on the column type
				throw new IllegalArgumentException("Description is too long.");
			}
			if (image != null && !image.isEmpty()) {
				productDB.setImageData(productImageData.getBytes());
				productDB.setImageName(productImageData.getOriginalFilename());
			}
			productDB.setModifiedBy("Thuy");
			productRepository.save(productDB);
			// Add a flash attribute for the success message

		}

	}

	public Product getProductDetails(Integer id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));

		return product;

	}
	
	public Product getProductById(Integer id) {
		return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id)); 
	}
	
	   public Size addSize(String name, Integer productId, Double finalPrice) {
	        Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new EntityNotFoundException("Category not found"));

	        Size size = new Size();
	        size.setName(name);
	        size.setProduct(product);
	        size.setSizePrice(finalPrice);

	        return sizeRepository.save(size);
	    }
	   
	   public List<Size> getSizes(int productId) {
	        return sizeRepository.findByProductId(productId);
	    }
	   public Size getSizeById(int sizeId) {
			// TODO Auto-generated method stub
			return sizeRepository.findById(sizeId).orElseThrow(() -> new EntityNotFoundException("Size not found with id: " + sizeId));
		}
	   public void updateSize(Size size) {
			sizeRepository.save(size);
			
		}
	   
	   @Transactional
	   public void deleteSize(Integer id) {
		   
		   Size size = sizeRepository.findById(id)
			        .orElseThrow(() -> new EntityNotFoundException("Size not found with id: " + id));
		// Check if the size is associated with any orders
		    List<OrderDetail> associatedOrders = orderDetailRepository.findBySizeId(id);
		    if (!associatedOrders.isEmpty()) {
		        throw new IllegalStateException("Cannot delete size."+size.getName()+" It is associated with " + associatedOrders.size() + " order(s).");
		    }
		    size.setProduct(null);
		    sizeRepository.delete(size);
		   
			
		}
	   
	   public List<Product> findAllProducts(){
		   return productRepository.findAll();
	   }

	public List<Product> findProductByCategoryId(Integer categoryId) {
		
		return productRepository.findProductByCategoryId(categoryId);
	}

	public Product findById(Integer newProductId) {
		// TODO Auto-generated method stub
		return productRepository.findById(newProductId).orElseThrow(() -> new EntityNotFoundException("Size not found with id: " + newProductId));
	}
	   
	   
}
