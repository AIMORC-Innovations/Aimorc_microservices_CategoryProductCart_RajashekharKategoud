package com.CategoryProductCart.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.CategoryProductCart.entity.Orders;
import com.CategoryProductCart.entity.ProductOrders;
import com.CategoryProductCart.entity.Products;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Orders, Integer> {

	
	/*
	 * @Query(value
	 * ="select c.category_id, c.category_name, p.product_id , p.product_name , p.product_description , p.product_price , O.quantity from product p join orders O on O.product_id = p.product_id  join category c on p.category_id = c.category_id where O.userid = :userid group by c.category_id, p.product_id, O.quantity"
	 * , nativeQuery = true) public List<ProductOrders> getAllOrders(@Param
	 * ("userid")int userid);
	 */
	@Modifying
	@Query(value="update orders set quantity = :quantity where product_id = :product_id and userid = :userid", nativeQuery = true)
	public int updateQuantity(@Param("quantity")int quantity,@Param("product_id") int product_id,@Param("userid") int userid);
	
	@Modifying
    @Query(value = "delete from orders where userid=:userid and product_id=:product_id", nativeQuery = true)
	public int delete(@Param ("userid")int userid, @Param ("product_id")int product_id);

	@Query(value ="select c.category_id, c.category_name, p.product_id , p.product_name , p.product_description , p.product_price , O.quantity from product p join orders O on O.product_id = p.product_id  join category c on p.category_id = c.category_id where O.userid = :userid group by c.category_id, p.product_id, O.quantity", nativeQuery = true)
	public List<Map<String, Object>> getAllOrders(@Param ("userid")int userid);
	
	@Modifying
	@Query(value = "insert into orders(quantity,product_id,userid) values (:quantity,:product_id,:userid)",nativeQuery = true)
	public int addToCartProductId(@Param("quantity")int quantity,@Param("product_id")int product_id,@Param("userid")int userid);
	
	@Query(value = "select product_id  from orders where product_id = :product_id and userid = :userid",nativeQuery = true)
	public Integer findByProductId(@Param("product_id") int product_id,@Param("userid") int userid);
	
	@Query(value = "select quantity from orders where product_id = :product_id and userid = :userid",nativeQuery = true)
	public Integer findByQuantity(@Param("product_id") int product_id,@Param("userid") int userid);
	
	@Query(value = "select userid from orders where userid = :userid and product_id = :product_id",nativeQuery = true)
	public Integer findByUserid(@Param("product_id") int product_id ,@Param("userid") int userid);

}
