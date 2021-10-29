package com.CategoryProductCart.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CategoryProductCart.entity.Category;



@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>
{
	 public List<Category> findAll();
	 
	
	 @Query(value = "select distinct(c.category_id), c.category_name from category c join product p on c.category_id = p.category_id join orders o on p.product_id = o.product_id join login l on l.userid = o.userid where l.userid=:userid", nativeQuery = true)
		public List<Map<String, Object>> getProductsBasedOnCategory(@Param("userid") int userid);
	 
	 @Query(value = "select distinct(c.category_id), c.category_name from category c join product p on c.category_id = p.category_id where p.category_id=:categoryid", nativeQuery = true)
		public List<Map<String, Object>> getProductsBasedOnCategory1(int categoryid);
}
