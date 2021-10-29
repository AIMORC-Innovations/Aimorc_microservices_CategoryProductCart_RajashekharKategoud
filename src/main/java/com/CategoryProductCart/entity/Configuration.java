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
@Table(name="configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int configuration_id;
	private String configuration_name;
	private int configuration_value;
	
}

