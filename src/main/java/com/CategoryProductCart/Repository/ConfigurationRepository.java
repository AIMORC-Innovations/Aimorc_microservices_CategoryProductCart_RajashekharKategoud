package com.CategoryProductCart.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.CategoryProductCart.entity.Configuration;

@Repository
@Transactional
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

	@Query(value="select configuration_value from configuration",nativeQuery = true)
	public int findByConfigurationValue();
}
