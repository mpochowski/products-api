package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.models.database.ProductEntity;
import com.godaddy.uk.productsapi.models.database.SubscriptionEntity;
import com.godaddy.uk.productsapi.models.database.UserEntity;
import com.godaddy.uk.productsapi.repositories.SubscriptionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class SubscriptionsServiceTest {

    @Mock
    private SubscriptionsRepository subscriptionsRepository;

    @InjectMocks
    private SubscriptionsService subscriptionsService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateSubscription() {
        // given
        UserEntity loggedInUser = new UserEntity();
        ProductEntity product = new ProductEntity();
        product.setTermLength(Period.ofMonths(1));

        // when
        subscriptionsService.createSubscription(loggedInUser, product);

        // then: verify it was saved
        ArgumentCaptor<SubscriptionEntity> captor = ArgumentCaptor.forClass(SubscriptionEntity.class);
        verify(this.subscriptionsRepository).save(captor.capture());
        // then: verify the details
        SubscriptionEntity subscription = captor.getValue();
        assertNotNull(subscription);
        assertEquals(product, subscription.getProduct());
        assertEquals(loggedInUser, subscription.getOwner());
        assertTrue(subscription.isActive());
        assertTrue(LocalDateTime.now().isBefore(subscription.getExpirationDate()));
    }

}