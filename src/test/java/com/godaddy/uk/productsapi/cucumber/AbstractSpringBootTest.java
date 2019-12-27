package com.godaddy.uk.productsapi.cucumber;

import com.godaddy.uk.productsapi.cucumber.clients.BasketApiClient;
import com.godaddy.uk.productsapi.models.BasketState;
import com.godaddy.uk.productsapi.models.api.Basket;
import com.godaddy.uk.productsapi.models.api.Product;
import com.godaddy.uk.productsapi.models.api.Subscription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Provides utility methods and common code for step definitions.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractSpringBootTest {

    /**
     * Verifies if expected products has been added to the basket.
     *
     * @param client             the basket client to be used for verification
     * @param basketId           id of the basket
     * @param expectedProductIds list of expected product ids
     */
    protected void verifyExpectedProducts(@NotNull BasketApiClient client, int basketId, @NotNull List<Integer> expectedProductIds) {
        // load my basket
        Basket basket = client.getBasketById(basketId);

        // retrieve product ids
        List<Integer> basketProductIds = basket.getProducts()
                .stream()
                .map(Product::getId)
                .collect(toList());

        // assert products are as expected
        assertEquals(expectedProductIds, basketProductIds);
    }

    /**
     * Verifies if basket has expected value.
     *
     * @param client              the basket client to be used for verification
     * @param basketId            id of the basket
     * @param expectedBasketValue expected basket value
     */
    protected void verifyBasketValue(@NotNull BasketApiClient client, int basketId, double expectedBasketValue) {
        Basket basket = client.getBasketById(basketId);
        assertEquals(expectedBasketValue, basket.getBasketValue(), 0);
    }

    /**
     * Updates basket state to desired one.
     *
     * @param client         the basket client to be used for verification
     * @param basketId       id of the basket
     * @param newStateString new required state as string
     */
    protected void updateBasketState(@NotNull BasketApiClient client, int basketId, @NotNull String newStateString) {
        // update basket state to required one
        BasketState desiredBasketState = BasketState.valueOf(newStateString);
        if (desiredBasketState != BasketState.COMPLETED) {
            client.updateBasketState(basketId, BasketState.valueOf(newStateString));
        } else {
            // if we need completed state, we need to update first to in checkout
            client.updateBasketState(basketId, BasketState.IN_CHECKOUT);
            client.updateBasketState(basketId, BasketState.COMPLETED);
        }
    }

    /**
     * Verifies active subscriptions.
     *
     * @param expectedSubscriptions        number of expected user subscriptions
     * @param expectedSubscribedProductIds list of expected subscribed products ids
     * @param actualSubscriptions          list of actual subscriptions
     */
    protected void verifyActiveSubscriptions(int expectedSubscriptions,
                                             @NotNull List<Integer> expectedSubscribedProductIds,
                                             @Nullable final List<Subscription> actualSubscriptions) {
        // verify number of subscriptions
        assertNotNull(actualSubscriptions);
        assertEquals(expectedSubscriptions, actualSubscriptions.size());

        // evaluate if all products are subscribed
        boolean allProductsSubscribed = expectedSubscribedProductIds
                .stream()
                .map(productId -> actualSubscriptions
                        .stream()
                        .filter(subscription -> subscription.getProduct().getId() == productId)
                        .findAny())
                .map(Optional::isPresent)
                .reduce(Boolean.TRUE, Boolean::logicalAnd);

        // verify
        assertTrue(allProductsSubscribed);
    }

}