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
import com.CategoryProductCart.entity.OrderItems;
import com.CategoryProductCart.entity.OrderItemsWithDetails;
import com.CategoryProductCart.entity.OrderItemsWithProductDetails;
import com.CategoryProductCart.entity.Orders;
import com.CategoryProductCart.entity.ProductOrders;
import com.CategoryProductCart.entity.Products;

@Repository
@Transactional
public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {

	//@Query(value = "SELECT * FROM order_items WHERE order_id = :orderId",nativeQuery = true)
	@Query(value = "SELECT oi.order_items_id, oi.order_id, oi.product_id, oi.quantity, oi.item_cost, p.product_name, p.product_price FROM order_items oi JOIN product p ON oi.product_id = p.product_id WHERE oi.order_id = :orderId", nativeQuery = true)
	public List<OrderItems> findByOrder_id(int orderId);
	
	

	

}
