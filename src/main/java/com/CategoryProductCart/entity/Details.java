package com.CategoryProductCart.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orderdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Details {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    @SequenceGenerator(name = "order_id_seq", sequenceName = "order_id_seq", allocationSize = 1)
    @Column(name = "order_id", updatable = false, nullable = false)
	private int order_id;
	private int userid;
	private int transaction_amount;
	private String transaction_date;
	public String getToken() {
		// TODO Auto-generated method stub
		return null;
	}
	public String setToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@OneToMany(mappedBy = "order_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<OrderItems> orderItems;
	private List<OrderItems> orderItems;
	
	
	
	
	
	
}