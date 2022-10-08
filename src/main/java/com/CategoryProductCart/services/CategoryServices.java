package com.CategoryProductCart.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.CategoryProductCart.Repository.CategoryRepository;
import com.CategoryProductCart.Repository.OrderRepository;
import com.CategoryProductCart.Repository.ProductRepository;
import com.CategoryProductCart.entity.Category;
import com.CategoryProductCart.entity.Orders;
import com.CategoryProductCart.entity.ProductOrders;
import com.CategoryProductCart.entity.Products;

@Service
public class CategoryServices {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public int getPageCount(int category_id) {
		return productRepository.countByCategoryId(category_id);
	}
	public int getPageCountForAllProducts(int category_id) {
		return productRepository.countByProductId(category_id);
	}




	public Map<String, Object> cartproducts(int userid) {
		Map<String, Object> ordersInfoBasedOnCategories = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> ordersCategoriesList = categoryRepository.getProductsBasedOnCategory(userid);
        
			for (Map<String, Object> eachCategoryMap : ordersCategoriesList) {
			
				int categoryId = (int) eachCategoryMap.get("category_id");
				String category_name = (String) eachCategoryMap.get("category_name");
				
				List<Map<String, Object>> allProductsBasedOnCategory = productRepository.findProducts(categoryId, userid);
				//System.out.println("allProductsBasedOnCategory"+allProductsBasedOnCategory);
				Map<String, Object> eachInfoMap = new HashMap<String, Object>();
				eachInfoMap.put("categoryId", categoryId);
				eachInfoMap.put("category_name", category_name);
				eachInfoMap.put("products", allProductsBasedOnCategory);
				String cid = String.valueOf(categoryId);
				ordersInfoBasedOnCategories.put(cid, eachInfoMap);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ordersInfoBasedOnCategories;
	}

//	public List<Products> viewCategory(ProductOrders products) {
//		List<Products> productlist = productRepository.findProducts(products.getCategory_id());
//		return productlist;
//	}

	 public List<Products> newProducts() { 
		 return productRepository.getNewproducts(); }
	 
	 public List<Products> getNewRelease() { 
		 return productRepository.getNewRelease(); }
	/*
	 * public Page<Products> findPaginated(int pageNo, int pageSize) { PageRequest
	 * pageable = PageRequest.of(pageNo - 1, pageSize); return
	 * this.productRepository.findAll(pageable); }
	 */
	/*
	 * public Page<Products> findPaginated(int pageNo, int pageSize, String
	 * sortField, String sortDirection) { Sort sort =
	 * sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
	 * Sort.by(sortField).ascending() : Sort.by(sortField).descending();
	 * 
	 * Pageable pageable = (Pageable) PageRequest.of(pageNo - 1, pageSize, sort);
	 * return this.productRepository.findAll(pageable); }
	 */
	
	
	public Page<Products> allProducts(Pageable pageable) {
		return productRepository.findAll(pageable);
	}
	
	/*
	 * public List<Products> allProductsPaginated(int start,int size) {
	 * Page<ProductOrders> allProducts =
	 * productRepository.findAll(firstPageWithTwoElements);
	 * 
	 * List<ProductOrders> allTenDollarProducts =
	 * productRepository.findAllByPrice(10, secondPageWithFiveElements);
	 * 
	 * return productRepository.findAll(); }
	 */

	/*
	 * public Orders addingProductToCart(Orders orders) { return
	 * orderRepository.save(orders); }
	 */
	/*
	 * @Transactional public int varyQuantity(int quantity, int product_id, int
	 * userid) { return orderRepository.updateQuantity(quantity, product_id,
	 * userid);
	 * 
	 * }
	 */
	@Transactional
	public Integer varyQuantity(int quantity, int product_id, int userid) {
		return orderRepository.updateQuantity(quantity, product_id, userid);

	}
	public int addProductsToUserid(int quantity, int product_id, int userid) {
		return orderRepository.addToCartProductId(quantity,product_id,userid);
		
	}


	public ResponseEntity<String> deleteFromCart(int userid, int product_id) {
		orderRepository.delete(userid, product_id);
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
	}

	public Page<Products> viewCategory(int category_id, Pageable pageable) {
		System.out.println("View category : "+ this.productRepository.findProducts(category_id, pageable));
		return this.productRepository.findProducts(category_id, pageable);

	}
	
	public Products getProductDetails(int product_id) {
		Products products = this.productRepository.findDetailsByProductId(product_id);
		return products;
	}
	
	public void editProductDetails(int product_id, String product_name, String product_description, double product_price, double max_quantity, String status) {
		int products = this.productRepository.updateProductDetailsByProductId(product_id, product_name, product_description, product_price, max_quantity, status);
		//return products;
	}

	public Page<Products> pageProducts(int category_id, Pageable Pageable) {
		return this.productRepository.findProductByPage(category_id, Pageable);
	}
	
	public Orders addingProductToCart(Orders orders) {
		return orderRepository.save(orders);
	}
	public Map<String, Object> cartproducts1(int categoryid, Pageable pageable) {
		Map<String, Object> ordersInfoBasedOnCategories = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> ordersCategoriesList = categoryRepository.getProductsBasedOnCategory1(categoryid);
        
			for (Map<String, Object> eachCategoryMap : ordersCategoriesList) {
			
				int categoryId = (int) eachCategoryMap.get("category_id");
				String category_name = (String) eachCategoryMap.get("category_name");
				
				List<Map<String, Object>> allProductsBasedOnCategory = productRepository.findProducts1(categoryId, pageable);
				Map<String, Object> eachInfoMap = new HashMap<String, Object>();
				eachInfoMap.put("categoryId", categoryId);
				eachInfoMap.put("category_name", category_name);
				eachInfoMap.put("products", allProductsBasedOnCategory);
				String cid = String.valueOf(categoryId);
				ordersInfoBasedOnCategories.put(cid, eachInfoMap);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ordersInfoBasedOnCategories;
	}

	public Products singleProductInfo(int product_id) {
		 return productRepository.productinfo(product_id);
		}


	
}
