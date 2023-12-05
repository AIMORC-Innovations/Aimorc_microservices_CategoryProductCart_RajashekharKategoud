package com.CategoryProductCart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsWithProductDetails {
	
	
	private int order_items_id;
	private int order_id;
	private int product_id;
	private int quantity;
	private Double item_cost;
	
	private String product_name;
	
	private Double product_price;
	
	
	
}