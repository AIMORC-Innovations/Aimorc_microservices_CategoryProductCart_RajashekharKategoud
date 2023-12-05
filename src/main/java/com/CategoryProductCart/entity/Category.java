package com.CategoryProductCart.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	@Column(name = "category_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_native")
	@GenericGenerator(name = "id_native", strategy = "native")
	@Column(name = "category_id", updatable = false, nullable = false)
	private int category_id;
	private String category_name;
	private String category_description;
	
	
	
	

}
