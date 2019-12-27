package com.godaddy.uk.productsapi.cucumber.basket.state;

import com.godaddy.uk.productsapi.cucumber.AbstractSpringBootTest;
import com.godaddy.uk.productsapi.cucumber.clients.BasketApiClient;
import com.godaddy.uk.productsapi.cucumber.clients.UserApiClient;
import com.godaddy.uk.productsapi.models.BasketState;
import com.godaddy.uk.productsapi.models.api.Basket;
import com.godaddy.uk.productsapi.models.api.User;
import com.godaddy.uk.productsapi.models.api.requests.CreateBasketRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BasketStateSteps extends AbstractSpringBootTest {

    @Autowired
    private BasketApiClient basketApiHttpClient;

    @Autowired
    private UserApiClient userApiHttpClient;

    private int basketId;

    @Given("I'm logged in as {string} using password {string} in brand {int}.")
    public void i_m_logged_in_as_using_password_in_brand(String username, String password, int brandId) {
        this.basketApiHttpClient.login(brandId, username, password);
        this.userApiHttpClient.login(brandId, username, password);
    }

    @Given("My basket is {string} with products:")
    public void my_basket_is_with_products(String desiredBasketStateString, List<Integer> productIds) {
        this.basketId = this.basketApiHttpClient.createBasket(new CreateBasketRequest(productIds)).getId();
        super.updateBasketState(this.basketApiHttpClient, this.basketId, desiredBasketStateString);
    }

    @Given("My basket is empty and {string}")
    public void my_basket_is_empty_and(String desiredBasketStateString) {
        this.basketId = this.basketApiHttpClient.createBasket(new CreateBasketRequest(emptyList())).getId();
        super.updateBasketState(this.basketApiHttpClient, this.basketId, desiredBasketStateString);
    }

    @When("I ask to create new empty basket.")
    public void i_ask_to_create_new_empty_basket() {
        this.basketId = this.basketApiHttpClient.createBasket(new CreateBasketRequest(emptyList())).getId();
    }

    @When("I ask to update basket state to {string}")
    public void i_ask_to_update_basket_state_to(String newBasketState) {
        this.basketApiHttpClient.updateBasketState(this.basketId, BasketState.valueOf(newBasketState));
    }

    @Then("The basket should be empty.")
    public void the_basket_should_be_empty() {
        super.verifyExpectedProducts(this.basketApiHttpClient, this.basketId, emptyList());
    }

    @Then("The basket value should be {double}")
    public void the_basket_value_should_be(double expectedBasketValue) {
        super.verifyBasketValue(this.basketApiHttpClient, this.basketId, expectedBasketValue);
    }

    @Then("I should own {int} active subscriptions for products:")
    public void i_should_own_active_subscriptions_for_products(final int expectedSubscriptions, final List<Integer> expectedSubscribedProductIds) {
        // load current user details and verify subscriptions
        final User user = this.userApiHttpClient.currentUserDetails();
        super.verifyActiveSubscriptions(expectedSubscriptions, expectedSubscribedProductIds, user.getSubscriptions());
    }

    @Then("The basket state should be {string}")
    public void the_basket_state_should_be(String newStateString) {
        // make sure basket is created
        Basket basket = this.basketApiHttpClient.getBasketById(basketId);
        assertNotNull(basket);

        // make sure basket has expected state
        assertEquals(BasketState.valueOf(newStateString), basket.getState());

        // logout
        this.userApiHttpClient.logout();
        this.basketApiHttpClient.logout();
    }

}