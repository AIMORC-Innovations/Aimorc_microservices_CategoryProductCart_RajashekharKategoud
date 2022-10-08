package com.CategoryProductCart.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int product_id;
	private String product_name;
	private String product_description;
	private double product_price;
	private int category_id;
	private double max_quantity;
	private String status;
	
	
	
	

}
