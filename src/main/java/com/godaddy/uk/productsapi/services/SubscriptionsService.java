package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.models.database.ProductEntity;
import com.godaddy.uk.productsapi.models.database.SubscriptionEntity;
import com.godaddy.uk.productsapi.models.database.UserEntity;
import com.godaddy.uk.productsapi.repositories.SubscriptionsRepository;
import com.godaddy.uk.productsapi.utilities.Loggable;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SubscriptionsService implements Loggable {

    @Autowired
    private SubscriptionsRepository subscriptionsRepository;

    /**
     * Creates subscription for given product.
     *
     * @param loggedInUser currently logged in user
     * @param product      the product to create subscription for
     */
    @Transactional
    public void createSubscription(@NotNull UserEntity loggedInUser, @NotNull ProductEntity product) {
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setProduct(product);
        subscription.setOwner(loggedInUser);
        subscription.setActive(true);
        subscription.setExpirationDate(LocalDateTime.now().plus(product.getTermLength()));
        this.subscriptionsRepository.save(subscription);
    }

}
