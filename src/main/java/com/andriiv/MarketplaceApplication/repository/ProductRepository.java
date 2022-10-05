package com.andriiv.MarketplaceApplication.repository;

import com.andriiv.MarketplaceApplication.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Roman_Andriiv
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
