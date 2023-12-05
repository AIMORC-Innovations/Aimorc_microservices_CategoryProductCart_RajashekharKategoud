package com.CategoryProductCart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="sku")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUnit {
	
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	//@Column(name = "sku_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_native")
	@GenericGenerator(name = "id_native", strategy = "native")
	@Column(name = "sku_id", updatable = false, nullable = false)
	private int sku_id;
    private int product_id;
	private String product_address1;
	private String product_address2;
	private String product_city;
	private String product_state;
	private String product_country;
	private String product_zip;
	private int number_of_products;
	private int sold_products;
	private int available_products;
	
	
	
	

}
