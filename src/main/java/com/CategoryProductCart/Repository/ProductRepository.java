package com.CategoryProductCart.Repository;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.CategoryProductCart.entity.Orders;
import com.CategoryProductCart.entity.ProductOrders;
import com.CategoryProductCart.entity.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products,Integer>
{

	
	  // public List<Products> findAll(Pageable pageable);

		/*
		 * @Query(value = "select * from product where category_id=?", nativeQuery =
		 * true) public List<Products> findProducts(@Param("category_id") int
		 * category_id);
		 */
		
		  @Query(
		  value="select p.product_id, p.product_description, p.product_name,p.product_price, o.quantity from orders o join product p on p.product_id = o.product_id join login l on l.userid = o.userid where p.category_id= :category_id and l.userid= :userid order by p.product_name asc"
		  , nativeQuery = true) public List<Map<String, Object>>
		  findProducts(@Param("category_id") int category_id, @Param("userid")int
		  userid);
		 
	@Query(value="select p.product_id, p.product_name , p.product_description , p.product_price,  p.category_id from  product p   where p.category_id = :category_id", nativeQuery = true)
	public Page<Products> findProducts( @Param("category_id")  int category_id, Pageable pageable);


		 @Query(value = "select * from product where product_id=?", nativeQuery = true)
		 public List<Map<String, Object>> findorders(@Param ("product_id") Optional<Orders> product_id);

	    	public int save(Orders orders);
	    	
	    	@Query(value="select count(p.category_id) from product p where p.category_id=:category_id  ", nativeQuery = true)
	    	public int countByCategoryId(int category_id);
	    	
	    	@Query(value="select count(category_id) from product  ", nativeQuery = true)
	    	public int countByProductId(int category_id);

			/*
			 * @Query(value="select count(product_id) from product   ", nativeQuery = true)
			 * public List<Products> countPage ();
			 */

	    	@Query(value="select p.product_id, p.product_name , p.product_description , p.product_price, c.category_name, c.category_id from  product p join category c on c.category_id = p.category_id where p.category_id = ?", nativeQuery = true)
			public List<Products> findProducts(@Param("category_id") int category_id);
	    	
	    	@Modifying
			@Query(value="select p.product_id, p.product_name , p.product_description , p.product_price, p.category_id from product p where p.istrendy=true",nativeQuery = true) 
			  public List<Products> getNewproducts();
	    	@Modifying
			@Query(value="select p.product_id, p.product_name , p.product_description , p.product_price, p.category_id from product p where p.isnextrelease=true",nativeQuery = true) 
			  public List<Products> getNewRelease();
	    	  
	    	
			@Query(value="select p.product_id, p.product_name , p.product_description , p.product_price, p.category_id from product p where p.category_id=:category_id ",nativeQuery = true)//Limit 2
	    	public Page<Products> findProductByPage(@Param("category_id")int category_id, Pageable Pageable);
	    	 
			@Query(value="select product_id, product_name , product_description , product_price, category_id from product where product_id=:product_id",nativeQuery = true) 
			public Products productinfo(int product_id);
	    	 
	    
			/*
			 * //@Modifying
			 * 
			 * @Query(value
			 * =" select p.product_id, p.product_name , p.product_description , p.product_price, c.category_name, c.category_id from  product p join category c on c.category_id = p.category_id where p.category_id = ? "
			 * , nativeQuery = true) public List<Object> findProducts(@Param("category_id")
			 * int category_id);
			 */

			@Query(value="select p.product_id, p.product_name , p.product_description , p.product_price,  p.category_id from  product p   where p.category_id = :category_id", nativeQuery = true)
			public List<Map<String, Object>> findProducts1( @Param("category_id")  int category_id, Pageable pageable);
}
