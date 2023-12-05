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
import com.CategoryProductCart.Repository.OrderDetailsRepository;
import com.CategoryProductCart.Repository.OrderDetailsRepository.OrderDetailsWithUserDetailsProjection;
import com.CategoryProductCart.Repository.OrderItemsRepository;
import com.CategoryProductCart.Repository.OrderRepository;
import com.CategoryProductCart.Repository.ProductRepository;
import com.CategoryProductCart.Repository.StockUnitRepository;
import com.CategoryProductCart.entity.Category;
import com.CategoryProductCart.entity.Details;
import com.CategoryProductCart.entity.OrderDetailsWithUserDetails;
import com.CategoryProductCart.entity.OrderItems;
import com.CategoryProductCart.entity.OrderItemsWithDetails;
import com.CategoryProductCart.entity.OrderItemsWithProductDetails;
import com.CategoryProductCart.entity.Orders;
import com.CategoryProductCart.entity.ProductOrders;
import com.CategoryProductCart.entity.Products;
import com.CategoryProductCart.entity.StockUnit;

@Service
public class CategoryServices {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	
	@Autowired
	private OrderItemsRepository orderItemsRepository;
	
	
	@Autowired
	private StockUnitRepository stockUnitRepository;

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}
	
//	public List<Category> getAllCategoryNames(){
//		return categoryRepository.getCategoryNames();
//	}

	public int getPageCount(int category_id) {
		return productRepository.countByCategoryId(category_id);
	}
	public int getPageCountForAllProducts(int category_id) {
		return productRepository.countByProductId(category_id);
	}
	
	public StockUnit stockunitaddressdetails(StockUnit stockUnit) {
		System.out.println(stockUnit.getProduct_address1()+" "+ stockUnit.getProduct_address2());
		//return stockUnitRepository.save(stockUnit.getProduct_id(), stockUnit.getProduct_address1(), stockUnit.getProduct_address2(), stockUnit.getProduct_city(), stockUnit.getProduct_state(), stockUnit.getProduct_country(), stockUnit.getProduct_zip());
		return stockUnitRepository.save(stockUnit);
	}
	
	public int addNewCategory(Category category) {
		System.out.println(category.getCategory_id());
		return categoryRepository.saveNewCategory(/*category.getCategory_id(),*/ category.getCategory_name(), category.getCategory_description());
	}
	
	public List<Map<StockUnit, Object>> getStockUnitAddress(/*int sku_id,*/ int product_id, String product_address1,
			String product_address2, String product_city, String product_country, String product_state,
			String product_zip) {
		// TODO Auto-generated method stub
		return stockUnitRepository.fetchStockUnitAddresses(/*sku_id,*/ product_id, product_address1, product_address2,product_city, product_country,product_state, product_zip);
	}
	
	public ResponseEntity<String> editStockUnitAddress(StockUnit stockUnit) {
		stockUnitRepository.updateSKUAddressDetails(stockUnit.getSku_id(), stockUnit.getAvailable_products(), stockUnit.getProduct_address1(), stockUnit.getProduct_address2(), stockUnit.getProduct_city(), stockUnit.getProduct_state(), stockUnit.getProduct_country(), stockUnit.getProduct_zip());
		return new ResponseEntity<String>("Details updated succesfully", HttpStatus.OK);
	}
	
	public ResponseEntity<String> deleteStockUnitAddress(StockUnit stockUnit) {
		stockUnitRepository.deleteStockUnitAddress(stockUnit.getSku_id());
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
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
	
	public StockUnit getSKUAddressBasedOnSkuId(int sku_id) {
		StockUnit stockUnit = this.stockUnitRepository.findSKUAddressBasedOnSkuId(sku_id);
		return stockUnit;
	}
	
	public void editProductDetails(int category_id, int product_id, String product_name, String product_description, double product_price, double max_quantity, String status) {
		int products = this.productRepository.updateProductDetailsByProductId(category_id, product_id, product_name, product_description, product_price, max_quantity, status);
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

	

//	public int storeOrderDetails(int order_id, int userid, int transaction_amount, String transaction_date) {
//		return orderDetailsRepository.storeOrderDetails(order_id, userid, transaction_amount, transaction_date);
//	}
	
//	public int storeOrderDetails(Details details) {
//        return orderDetailsRepository.storeOrderDetails(details).getOrder_id();
//    }
	public int storeOrderDetails(Details details) {
        Details savedDetails = orderDetailsRepository.save(details);
        return savedDetails.getOrder_id();
    }

	public void storeOrderItem(OrderItems orderitem) {
		OrderItems savedOrderItems = orderItemsRepository.save(orderitem);
	}

	public List<Details> getOrdersByUserId(int userId) {
	    return orderDetailsRepository.findByUserid(userId);
	}

	public List<OrderItems> getOrderItemsByOrderId(int orderId) {
	    return orderItemsRepository.findByOrder_id(orderId);
	}

	public Details getDetailsByOrderId(int orderId) {
		return orderDetailsRepository.findByOrderid(orderId);
	}

//	public OrderDetailsWithUserDetails getDetailsByOrderIdAndUserId(int orderId, int userid) {
//		return orderDetailsRepository.findByOrderIdAndUserDetails(orderId, userid);
//	}
	public OrderDetailsWithUserDetails getDetailsByOrderIdAndUserId(int orderId, int userid) {
	    OrderDetailsWithUserDetailsProjection projection = orderDetailsRepository.findByOrderIdAndUserDetails(orderId, userid);
	    return new OrderDetailsWithUserDetails(projection.getOrder_id(), projection.getUserid(),
	            projection.getTransaction_amount(), projection.getTransaction_date(),
	            projection.getFirstname(), projection.getLastname(),
	            projection.getAddress1(), projection.getAddress(),
	            projection.getCity(), projection.getState(), projection.getZip());
	}


	

	

	


	
}
