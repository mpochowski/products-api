package com.godaddy.uk.productsapi.repositories;

import com.godaddy.uk.productsapi.models.database.BasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketsRepository extends JpaRepository<BasketEntity, Integer> {

    /**
     * Retrieves basket by basket id and basket owner id.
     *
     * @param basketId id of the basket
     * @param ownerId  id of the user, who is the owner of the basket
     * @return optional of basket entity
     */
    Optional<BasketEntity> findByIdAndOwnerId(int basketId, int ownerId);

}