package com.godaddy.uk.productsapi.repositories;

import com.godaddy.uk.productsapi.models.database.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionsRepository extends JpaRepository<SubscriptionEntity, Integer> {
}