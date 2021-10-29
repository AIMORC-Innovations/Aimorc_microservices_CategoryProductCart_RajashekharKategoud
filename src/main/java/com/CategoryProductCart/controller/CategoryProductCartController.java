package com.CategoryProductCart.controller;


import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;

import com.CategoryProductCart.Repository.ConfigurationRepository;
import com.CategoryProductCart.Repository.OrderRepository;
import com.CategoryProductCart.Repository.ProductRepository;
import com.CategoryProductCart.entity.Category;

import com.CategoryProductCart.entity.Orders;
import com.CategoryProductCart.entity.ProductOrders;
import com.CategoryProductCart.entity.Products;
import com.CategoryProductCart.services.CategoryServices;

import com.google.gson.Gson;

@RestController
@CrossOrigin("*")


public class CategoryProductCartController {

	@Autowired
	private ConfigurationRepository configRepo;
	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private ProductRepository productrepository;
	
	@Autowired
	private OrderRepository orderrepo;
	
	RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/allCategories")
	@ResponseBody
	public List<Category> getAllCategories() {
		return this.categoryServices.getAllCategories();
		

	}
	@RequestMapping(value = "/getProductsFromCart", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
   public Map<String, Object> getProductsFromCart(@RequestBody ProductOrders orders) {
			String tokenUsername =
				  restTemplate.postForObject("http://localhost:8081/decodeToken", orders.getToken(), String.class);
		Orders order = new Orders();
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername,
				String.class);
		int userid = Integer.parseInt(result);
		order.setUserid(userid);
		return this.categoryServices.cartproducts(userid);
	}
	
	@RequestMapping(value="/singleProductInfo", method = RequestMethod.POST, produces = "application/json")
		@ResponseBody
		public Products singleProductInfo(@RequestBody ProductOrders product) {
		 String tokenUsername =restTemplate.postForObject("http://localhost:8081/decodeToken", product.getToken(), String.class);
		 return this.categoryServices.singleProductInfo(product.getProduct_id());
			

	 }

	@RequestMapping(value = "/getPageCountforCategory")
	@ResponseBody
	public int getPageCount(@RequestBody ProductOrders product) {
		int pagesize = configRepo.findByConfigurationValue();

		product.setToken(product.getToken());
		if(product.getCategory_id() == 0) {
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", product.getToken(),
					String.class);

			String userId = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int  TotalProduct = this.categoryServices.getPageCountForAllProducts(product.getCategory_id());
		
			
			int count = TotalProduct%pagesize;
			if(count==0)
			{
				int val =TotalProduct/pagesize;
				return val;
			}
			else if(count>=1 ||	 count<=10)
			{
				int val1 =TotalProduct/pagesize;
				int val = val1+1;
				return val;
			}
	
			return count;
		}
		else {
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", product.getToken(),
					String.class);

			String userId = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int  TotalProduct =  this.categoryServices.getPageCount(product.getCategory_id());
		
			int count = TotalProduct%pagesize;
			if(count==0)
			{
				int val =TotalProduct/pagesize;
				return val;
			}
			else if(count>=1 ||	 count<=10)
			{
				int val1 =TotalProduct/pagesize;
				int val = val1+1;
				return val;
			}
	
			return count;

			
		}
		
		

	}

	
	@RequestMapping(value = "/viewCategory")
	@ResponseBody
	public Page<Products> viewCategory(@RequestBody ProductOrders product) {



		product.setToken(product.getToken());
		int pagesize = configRepo.findByConfigurationValue();
		if(product.getCategory_id()== 0) 
		{
			//178,9 same ,select count of product(DAO),try catch int var called result /PAGESIZE;  
			
			 Pageable pageable = PageRequest.of(product.getPage(),pagesize);

			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", product.getToken(),
					String.class);

			String userId = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			
			return this.categoryServices.allProducts(pageable);
		}
		else {
//SELECT COUNT WHERE CATEGORY iD TRY CATCH int var called result/pagesize;
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", product.getToken(),
					String.class);

			String userId = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			Pageable pageable = PageRequest.of(product.getPage(), pagesize);
			return this.categoryServices.viewCategory(product.getCategory_id(),pageable);
		}
		
	}
	 
	 
	 @RequestMapping(value = "/releaseTrendingProducts")
	  
	  @ResponseBody 
	  public List<Products> newProducts() { return
	  this.categoryServices.newProducts();
	  
	  }
	 @RequestMapping(value = "/nextReleaseProducts")
	  
	  @ResponseBody 
	  public List<Products> getNewRelease() { return
	  this.categoryServices.getNewRelease();
	  
	  }
	

	public Object decodeToken(String token)
	{
		 Base64.Decoder decoder = Base64.getDecoder();
			
			String[] chunks = token.split("\\.");

			String header = new String(decoder.decode(chunks[0]));
			String payload = new String(decoder.decode(chunks[1]));
			

			
			 Map<String, Object> map = new Gson().fromJson(payload, Map.class);
			 Object output = null;
			 
			 for (Map.Entry<String, Object> entry : map.entrySet()) {
			    	if(entry.getKey().equals("sub"))
			    	{
			    		output = entry.getValue();
			    	}
			       
			    }
		
			 return output;
	}
	 @RequestMapping(value = "/getcategoryname", method = RequestMethod.POST, produces = "application/json")
		@ResponseBody
	   public Map<String, Object> getcatregoryname(@RequestBody ProductOrders product) {
				String tokenUsername =
					  restTemplate.postForObject("http://localhost:8081/decodeToken", product.getToken(), String.class);
			Orders order = new Orders();
			int pagesize = configRepo.findByConfigurationValue();
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername,
					String.class);
			int userid = Integer.parseInt(result);
			order.setUserid(userid);
			Pageable pageable = PageRequest.of(product.getPage(), pagesize);
			return this.categoryServices.cartproducts1(product.getCategory_id(),pageable);
		}

	


	@RequestMapping(value = "/addToCart", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void addingProductToCart(@RequestBody ProductOrders orders) {
		String token = orders.getToken();
		String tokenUsername =
				  restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		Orders order = new Orders();
		order.setProduct_id(orders.getProduct_id());
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername,
				String.class);
		int userid = Integer.parseInt(result);
		order.setUserid(userid);
		order.setQuantity(orders.getQuantity());
		Integer existingUserId = orderrepo.findByUserid(userid);
		Integer existingProductId = orderrepo.findByProductId(order.getProduct_id(), userid);
		Integer existingQuantity = orderrepo.findByQuantity(order.getProduct_id(), userid);
		if (existingUserId == null) {
			categoryServices.addingProductToCart(order);
		} 
		else if(existingQuantity!=null) {
			int quantity = existingQuantity + orders.getQuantity();
			order.setQuantity(quantity);
			categoryServices.varyQuantity(order.getQuantity(), order.getProduct_id(), order.getUserid());
		}
		
		else
		{
			categoryServices.addProductsToUserid(order.getQuantity(),order.getProduct_id(), order.getUserid());
		}
		
		
	}

	@RequestMapping(value = "/quantityVariation", method = RequestMethod.POST, produces = "application/json")
	public int varyQuantity(@RequestBody ProductOrders orders) {
		String tokenUsername =
				  restTemplate.postForObject("http://localhost:8081/decodeToken", orders.getToken(), String.class);
		Orders order = new Orders();
		order.setProduct_id(orders.getProduct_id());
		order.setQuantity(orders.getQuantity());
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject("http://localhost:8081/getUserId",tokenUsername,
				String.class);
		int userid = Integer.parseInt(result);
		order.setUserid(userid);
		return this.categoryServices.varyQuantity(order.getQuantity(), order.getProduct_id(), order.getUserid());
	}

	

	@RequestMapping(value = "/removeItem", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> deleteFromCart(@RequestBody ProductOrders orders) {
		String tokenUsername =
				  restTemplate.postForObject("http://localhost:8081/decodeToken", orders.getToken(), String.class);
		Orders order = new Orders();
		order.setProduct_id(orders.getProduct_id());
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername,
				String.class);
		int userid = Integer.parseInt(result);
		order.setUserid(userid);
		return this.categoryServices.deleteFromCart(order.getUserid(),order.getProduct_id());

	}
	


}
 