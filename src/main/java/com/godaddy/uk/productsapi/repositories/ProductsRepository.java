package com.godaddy.uk.productsapi.repositories;

import com.godaddy.uk.productsapi.models.database.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Integer> {
}