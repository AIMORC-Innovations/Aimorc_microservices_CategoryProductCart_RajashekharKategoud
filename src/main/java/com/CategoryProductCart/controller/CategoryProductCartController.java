package com.CategoryProductCart.controller;


//import java.awt.PageAttributes.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.http.HttpHeaders;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.swing.text.Document;

//import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;

import com.CategoryProductCart.Repository.ConfigurationRepository;
import com.CategoryProductCart.Repository.OrderRepository;
import com.CategoryProductCart.Repository.ProductRepository;
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
import com.CategoryProductCart.services.CategoryServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.itextpdf.text.Image;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;

import org.springframework.http.MediaType;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;





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

	public void RouteServiceController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
    }
    
	@RequestMapping(value = "/allCategories")
	@ResponseBody
	public List<Category> getAllCategories() {
		System.out.println(this.categoryServices.getAllCategories());
		return this.categoryServices.getAllCategories();
	}
	
	
//	@RequestMapping(value="/getCategories")
//	@ResponseBody
//	public List<Category> getCategories(@RequestBody List<Category> category) {
//		category = categoryServices.getAllCategoryNames();
//		System.out.println("-------"+category+"-------");
//		return category;
//	}
	
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
		System.out.println("getproducts from cart"+this.categoryServices.cartproducts(userid));
		return this.categoryServices.cartproducts(userid);
	}
	
	@RequestMapping(value = "/addStockUnitAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public StockUnit addStockUnitAddress(@RequestBody StockUnit stockUnit){ 

		System.out.println(stockUnit.getProduct_address1()+" "+stockUnit.getProduct_address2()+""+stockUnit.getProduct_city());
		return this.categoryServices.stockunitaddressdetails(stockUnit);
		
	}  
	
	@RequestMapping(value = "/addNewCategory", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void addNewCategory(@RequestBody Category category) {
		
		this.categoryServices.addNewCategory(category);
	}
	
	
	@RequestMapping(value = "/getStockUnitAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  List<Map<StockUnit, Object>> getScheduledAddress(@RequestBody StockUnit stockUnit){
		
		return this.categoryServices.getStockUnitAddress(/* stockUnit.getSku_id(),*/ stockUnit.getProduct_id(), stockUnit.getProduct_address1(), stockUnit.getProduct_address2(), stockUnit.getProduct_city(), stockUnit.getProduct_country(), stockUnit.getProduct_state(), stockUnit.getProduct_zip());
	}	
	
	@RequestMapping(value = "/deleteStockUnitAddress", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> deleteStockUnitAddress(@RequestBody StockUnit stockUnit) {
		return this.categoryServices.deleteStockUnitAddress(stockUnit);
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
	
	@RequestMapping(value="/getProductDetailsBasedOnProductId")
	@ResponseBody
	public Products getProductDetails(@RequestBody Products products) {
		products = categoryServices.getProductDetails(products.getProduct_id());
		return products;
	}
	
	
	
	@RequestMapping(value="/getSKUAddressBasedOnSkuId")
	@ResponseBody
	public StockUnit getSKUAddressBasedOnSkuId(@RequestBody StockUnit stockUnit) {
		stockUnit = categoryServices.getSKUAddressBasedOnSkuId(stockUnit.getSku_id());
		return stockUnit;
	}
	
	@PostMapping("/editStockUnitAddress")
	@ResponseBody
	public ResponseEntity<String> editStockUnitAddress(@RequestBody StockUnit stockUnit){
		
		return this.categoryServices.editStockUnitAddress(stockUnit);
	}
		
	
	
	@RequestMapping(value="/editProductDetails")
	@ResponseBody
	public void editProductDetails(@RequestBody Products products) {
		//products = categoryServices.editProductDetails(products.getProduct_id(), products.getProduct_name(), products.getProduct_description(), products.getProduct_price(), products.getMax_quantity());
		this.categoryServices.editProductDetails(products.getCategory_id(), products.getProduct_id(), products.getProduct_name(), products.getProduct_description(), products.getProduct_price(), products.getMax_quantity(), products.getStatus());
		
		//return products;
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
			System.out.println("inside if : " + this.categoryServices.allProducts(pageable));
			return this.categoryServices.allProducts(pageable);
		}
		else {
//SELECT COUNT WHERE CATEGORY iD TRY CATCH int var called result/pagesize;
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", product.getToken(),
					String.class);

			String userId = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			Pageable pageable = PageRequest.of(product.getPage(), pagesize);
			System.out.println("inside else : " + this.categoryServices.viewCategory(product.getCategory_id(),pageable));
			return this.categoryServices.viewCategory(product.getCategory_id(),pageable);
		}
		
	}
	
	
	
	 
	 
	 @RequestMapping(value = "/releaseTrendingProducts")
	  
	  @ResponseBody 
	  public List<Products> newProducts() { 
		 return this.categoryServices.newProducts();
	  
	  }
	 @RequestMapping(value = "/nextReleaseProducts")
	  
	  @ResponseBody 
	  public List<Products> getNewRelease() { 
		 return this.categoryServices.getNewRelease();
	  
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
		System.out.println(userid+"    ---userid---   ");
		order.setUserid(userid);
		order.setQuantity(orders.getQuantity());
		System.out.println("order.getProduct_id()------>"+order.getProduct_id());
		Integer existingUserId = orderrepo.findByUserid(order.getProduct_id(), userid);
		System.out.println(existingUserId+"   -----existingUserId------    ");
		Integer existingProductId = orderrepo.findByProductId(order.getProduct_id(), userid);
		System.out.println(existingProductId+"        ----existingProductId----    ");
		Integer existingQuantity = orderrepo.findByQuantity(order.getProduct_id(), userid);
		System.out.println(existingQuantity+"        ----existingQuantity----    ");
		if (existingUserId != null) { //existingUserId == null
			categoryServices.addingProductToCart(order);
		} 
		else if(existingQuantity!=null) { //else if
			int quantity = existingQuantity + orders.getQuantity();
			order.setQuantity(quantity);
			categoryServices.varyQuantity(order.getQuantity(), order.getProduct_id(), order.getUserid());
		}
		
		else
		{
			categoryServices.addProductsToUserid(order.getQuantity(),order.getProduct_id(), order.getUserid());
		}
		
		
	}
	
	@RequestMapping(value = "/storeOrderDetails", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, Object> storeOrderDetails(@RequestBody Map<String, Object> requestData) {
	    String token = (String) requestData.get("token");
	    int transaction_amount = (int) requestData.get("transaction_amount");
	    String transaction_date = (String) requestData.get("transaction_date");
		
		Details details = new Details(); // Create an instance of the Details class
	    details.setToken(token);
	    details.setTransaction_amount(transaction_amount);
	    details.setTransaction_date(transaction_date);
	    
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		details.setUserid(userid);
		int orderId =  categoryServices.storeOrderDetails(details);
		 Map<String, Object> response = new HashMap<>();
		    response.put("order_id", orderId);
		    return response;	
	}
	
	@RequestMapping(value = "/storeOrderItem", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void storeOrderItem(@RequestBody Map<String, Object> requestData) {
	    int order_id = (int) requestData.get("order_id");
	    int product_id = (int) requestData.get("product_id");
	    int quantity = (int) requestData.get("quantity");
//	    double item_cost = (double) requestData.get("item_cost");
	    double item_cost = ((Number) requestData.get("item_cost")).doubleValue();
		
		OrderItems orderitem = new OrderItems(); // Create an instance of the Details class
		orderitem.setOrder_id(order_id);
		orderitem.setProduct_id(product_id);
		orderitem.setQuantity(quantity);
		orderitem.setItem_cost(item_cost);
	    
		categoryServices.storeOrderItem(orderitem);
	}
	
	@RequestMapping(value = "/getMyOrders", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<Details> getMyOrders(@RequestBody Map<String, Object> requestData) {
		String token = (String) requestData.get("token");
		Details details = new Details(); // Create an instance of the Details class
	    details.setToken(token);
	    String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		System.out.println(userid+"------>");
	    List<Details> orders = categoryServices.getOrdersByUserId(userid);
	    for (Details order : orders) {
	        List<OrderItems> orderItems = categoryServices.getOrderItemsByOrderId(order.getOrder_id());
	        order.setOrderItems(orderItems);
	    }
	    System.out.println(orders);
	    return orders;
	}
	
	@RequestMapping(value = "/getDetailsBasedOnOrderId", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Details getDetailsBasedOnOrderId(@RequestBody Map<String, Object> requestData) {
	    int orderId = (int) requestData.get("order_id");
	    Details orderDetails = categoryServices.getDetailsByOrderId(orderId);
	    
	    List<OrderItems> orderItems = categoryServices.getOrderItemsByOrderId(orderId);
	    orderDetails.setOrderItems(orderItems);
	    
	    return orderDetails;
	}
	
	@RequestMapping(value = "/downloadInvoice", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> downloadInvoice(@RequestBody Map<String, Object> requestData) throws MalformedURLException, IOException {
	    int orderId = (int) requestData.get("order_id");
	    System.out.println("1");
	    String token = (String) requestData.get("token");
	    System.out.println("1");
	    OrderDetailsWithUserDetails details = new OrderDetailsWithUserDetails(); // Create an instance of the Details class
	    System.out.println("1");
	    details.setToken(token);
	    System.out.println("1");
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		details.setUserid(userid);

	    // Fetch order details
		OrderDetailsWithUserDetails orderDetails = categoryServices.getDetailsByOrderIdAndUserId(orderId, userid);
	    List<OrderItems> orderItems = categoryServices.getOrderItemsByOrderId(orderId);
	    orderDetails.setOrderItems(orderItems);

	    // Create a PDF document
	    Document document = new Document();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    try {
	        PdfWriter.getInstance(document, baos);
	        document.open();

	        Image image = Image.getInstance("http://localhost:8080/AIMORCProject/images/aimorc_logo1.jpg");
	        image.setAlignment(Image.ALIGN_CENTER);
	        image.scaleToFit(100, 100);
	        document.add(image);

	        Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
	        Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA);

	        // Add order details to the PDF
	        document.add(new Paragraph());
	        document.add(new Paragraph("Order Number: " + orderDetails.getOrder_id(), normalFont));
	        document.add(new Paragraph());
	        document.add(new Paragraph("Invoice Date: " + orderDetails.getTransaction_date(), normalFont));
	        document.add(new Paragraph());
	        
	     // Add user details to the PDF
	        document.add(new Paragraph("Name: " + orderDetails.getFirstname() + " " + orderDetails.getLastname(), normalFont));
	        document.add(new Paragraph("Address: " + orderDetails.getAddress() + ", " + orderDetails.getAddress1()+" "+ orderDetails.getCity()+" "+ orderDetails.getState(), normalFont));
	        //document.add(new Paragraph(""));
	        document.add(Chunk.NEWLINE);
	        // Add a table for order items
	        PdfPTable table = new PdfPTable(5);
	        table.setWidthPercentage(100);
	        
	        table.addCell(new Phrase("Sl. No.", tableHeaderFont));
	        table.addCell(new Phrase("Product Name", tableHeaderFont));
	        table.addCell(new Phrase("Product Price", tableHeaderFont));
	        table.addCell(new Phrase("Quantity", tableHeaderFont));
	        table.addCell(new Phrase("Item Cost", tableHeaderFont));
	        
	        for (int i = 0; i < orderItems.size(); i++) {
	            OrderItems item = orderItems.get(i);
	            table.addCell(new Phrase(String.valueOf(i + 1), normalFont));
	            table.addCell(new Phrase(String.valueOf(item.getProduct_name()), normalFont));
	            table.addCell(new Phrase(String.valueOf(item.getProduct_price()), normalFont));
	            table.addCell(new Phrase(String.valueOf(item.getQuantity()), normalFont));
	            table.addCell(new Phrase(String.valueOf(item.getItem_cost()), normalFont));
	        }

	        table.addCell(new Phrase("Total Amount:", tableHeaderFont));
	        table.addCell("");
	        table.addCell("");
	        table.addCell("");
	        table.addCell(new Phrase(String.valueOf(orderDetails.getTransaction_amount()), normalFont));

	        document.add(table);
	        
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);
	        document.add(Chunk.NEWLINE);
	        
	        
	        Image image1 = Image.getInstance("http://localhost:8080/AIMORCProject/images/signature_for_invoice.jpg");
	        image1.setAlignment(Image.ALIGN_RIGHT);
	        image1.scaleToFit(100, 100);
	        document.add(image1);
	        
	        Font aimorcFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLUE);
	        Paragraph aimorcText = new Paragraph("Aimorc Innovations", aimorcFont);
	        aimorcText.setAlignment(Element.ALIGN_RIGHT);
	        document.add(aimorcText);

	        // Add the signature
	        Paragraph signature = new Paragraph();
	        signature.add(new Chunk("Signature           ", normalFont));
	        signature.setAlignment(Element.ALIGN_RIGHT);
	        document.add(signature);
	     
	        
	        
	        document.close();
	    } catch (DocumentException e) {
	        e.printStackTrace();
	    }

	    // Convert ByteArrayOutputStream to a byte array
	    byte[] pdfContents = baos.toByteArray();

	    // Set the content disposition header for attachment
	    org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "invoice.pdf");

	    // Return the PDF as a ResponseEntity
	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(pdfContents);
	}

	private char[] getProduct_name() {
		// TODO Auto-generated method stub
		return null;
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
 