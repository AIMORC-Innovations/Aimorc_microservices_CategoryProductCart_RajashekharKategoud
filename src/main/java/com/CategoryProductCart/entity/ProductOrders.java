package com.CategoryProductCart.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrders {
	private int category_id;
	private String category_name;
	private String username;
	private String product_name;
	private String product_description;
	private double product_price;
	private int product_id;
	private int quantity;
	private int page;
	private int pageSize;
		private String token;
	
	

}
