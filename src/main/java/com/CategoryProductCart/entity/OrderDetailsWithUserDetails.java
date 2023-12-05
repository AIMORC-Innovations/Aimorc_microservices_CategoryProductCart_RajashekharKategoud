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


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsWithUserDetails {
	
	private int order_id;
	private int userid;
	private int transaction_amount;
	private String transaction_date;
	private String firstname;
	private String lastname;
	private String address1;
	private String address;
	private String city;
	private String state;
	private String zip;
	public String getToken() {
		// TODO Auto-generated method stub
		return null;
	}
	public String setToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@OneToMany(mappedBy = "order_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;
	
	public OrderDetailsWithUserDetails(int order_id, int userid, int transaction_amount, String transaction_date,
            String firstname, String lastname, String address1, String address,
            String city, String state, String zip) {
			this.order_id = order_id;
			this.userid = userid;
			this.transaction_amount = transaction_amount;
			this.transaction_date = transaction_date;
			this.firstname = firstname;
			this.lastname = lastname;
			this.address1 = address1;
			this.address = address;
			this.city = city;
			this.state = state;
			this.zip = zip;
	}
	
	
	
	
	
}