package com.CategoryProductCart.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.CategoryProductCart.entity.StockUnit;


@Transactional
@Repository
public interface StockUnitRepository extends JpaRepository<StockUnit, Integer>{ 

	
	@Query(value = "insert into sku(available_products, product_address1,product_address2,product_city,product_state,product_country,product_zip,product_id) values (:available_products, :product_address1,:product_address2,:product_city,:product_state,:product_country,:product_zip,:product_id)",nativeQuery = true)
	public StockUnit save(int product_id, String product_address1, String product_address2, String product_city,
			String product_state, String product_country, String product_zip, int available_products);
	
	@Query(value="select * from sku where product_id=?",nativeQuery = true)
	public List<Map<StockUnit, Object>> fetchStockUnitAddresses( @Param("product_id") int product_id,@Param("product_address1") String product_address1,
			@Param("product_address2") String product_address2,@Param("product_city") String product_city,@Param("product_country") String product_country,@Param("product_state") String product_state,
			@Param("product_zip") String product_zip);

	@Modifying
    @Query(value = "delete from sku where sku_id=:sku_id", nativeQuery = true)
	public void deleteStockUnitAddress(int sku_id);

	@Query(value="select * from sku where sku_id=?", nativeQuery=true)
	public StockUnit findSKUAddressBasedOnSkuId(@Param("sku_id") int sku_id);

	@Modifying
	@Query(value = "UPDATE sku set available_products=:available_products, product_address1=:product_address1, product_address2=:product_address2, product_city=:product_city, product_state=:product_state, product_country=:product_country, product_zip=:product_zip where sku_id=:sku_id", nativeQuery = true)
	public void updateSKUAddressDetails(@Param("sku_id") int sku_id,@Param("available_products") int available_products, @Param("product_address1") String product_address1,@Param("product_address2") String product_address2,
			@Param("product_city") String product_city, @Param("product_state") String product_state,@Param("product_country") String product_country,@Param("product_zip") String product_zip);
	
	
}
