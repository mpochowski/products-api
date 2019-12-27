package com.godaddy.uk.productsapi.repositories;

import com.godaddy.uk.productsapi.models.database.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandsRepository extends JpaRepository<BrandEntity, Integer> {
}