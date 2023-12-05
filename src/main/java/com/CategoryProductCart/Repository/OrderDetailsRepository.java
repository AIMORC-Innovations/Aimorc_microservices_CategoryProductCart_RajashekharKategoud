package com.CategoryProductCart.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.CategoryProductCart.entity.Details;
import com.CategoryProductCart.entity.OrderDetailsWithUserDetails;
import com.CategoryProductCart.entity.Orders;
import com.CategoryProductCart.entity.ProductOrders;
import com.CategoryProductCart.entity.Products;

@Repository
@Transactional
public interface OrderDetailsRepository extends JpaRepository<Details, Integer> {

	@Query(value = "SELECT * FROM orderdetails WHERE userid = :userId ORDER BY order_id DESC",nativeQuery = true)
	public List<Details> findByUserid(int userId);
	
	@Query(value = "SELECT * FROM orderdetails WHERE order_id = :orderId",nativeQuery = true)
	public Details findByOrderid(int orderId);

//	@Query(value = "SELECT od.*, ud.firstname, ud.lastname, ud.address, ud.address1, ud.city, ud.state, ud.zip FROM orderdetails od JOIN registration ud ON od.userid = ud.userid WHERE od.order_id = :orderId AND od.userid = :userid", nativeQuery = true)
//	public OrderDetailsWithUserDetails findByOrderIdAndUserDetails(int orderId, int userid);

	@Query(value = "SELECT od.*, ud.firstname, ud.lastname, ud.address, ud.address1, ud.city, ud.state, ud.zip " +
		       "FROM orderdetails od JOIN registration ud ON od.userid = ud.userid " +
		       "WHERE od.order_id = :orderId AND od.userid = :userid", nativeQuery = true)
		public OrderDetailsWithUserDetailsProjection findByOrderIdAndUserDetails(int orderId, int userid);

		// Define the projection interface
		public interface OrderDetailsWithUserDetailsProjection {
		    int getOrder_id();
		    int getUserid();
		    int getTransaction_amount();
		    String getTransaction_date();
		    String getFirstname();
		    String getLastname();
		    String getAddress1();
		    String getAddress();
		    String getCity();
		    String getState();
		    String getZip();
		}


//	@Modifying
//	@Query(value = "insert into orderdetails(order_id, userid, transaction_amount, transaction_date) values (:order_id, :userid,:transaction_amount, :transaction_date)",nativeQuery = true)
//	public int storeOrderDetails(int order_id, int userid, int transaction_amount, String transaction_date);

	

	

}
