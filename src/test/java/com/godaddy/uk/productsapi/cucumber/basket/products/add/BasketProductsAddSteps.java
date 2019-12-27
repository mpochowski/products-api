package com.godaddy.uk.productsapi.cucumber.basket.products.add;

import com.godaddy.uk.productsapi.cucumber.AbstractSpringBootTest;
import com.godaddy.uk.productsapi.cucumber.clients.BasketApiClient;
import com.godaddy.uk.productsapi.models.api.requests.CreateBasketRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;

public class BasketProductsAddSteps extends AbstractSpringBootTest {

    @Autowired
    private BasketApiClient basketApiHttpClient;

    private int basketId;

    @Given("I'm logged in as {string} using password {string} in brand {int}.")
    public void i_m_logged_in_as_using_password_in_brand(String username, String password, int brandId) {
        this.basketApiHttpClient.login(brandId, username, password);
    }

    @Given("My basket has products:")
    public void my_basket_has_products(List<Integer> productIds) {
        this.basketId = this.basketApiHttpClient.createBasket(new CreateBasketRequest(productIds)).getId();
    }

    @Given("My basket is empty.")
    public void my_basket_is_empty() {
        this.basketId = this.basketApiHttpClient.createBasket(new CreateBasketRequest(emptyList())).getId();
    }

    @Given("My basket has {string} state.")
    public void basket_has_state(String newStateString) {
        super.updateBasketState(this.basketApiHttpClient, this.basketId, newStateString);
    }

    @When("I ask to add products to basket:")
    public void i_ask_to_add_products_to_basket(List<Integer> productIds) {
        this.basketApiHttpClient.addBasketProducts(this.basketId, productIds);
    }

    @Then("The basket value should be {double} and should contain products:")
    public void the_basket_value_should_be_and_should_contain_products(double expectedBasketValue, List<Integer> expectedProductIds) {
        super.verifyBasketValue(this.basketApiHttpClient, this.basketId, expectedBasketValue);
        super.verifyExpectedProducts(this.basketApiHttpClient, this.basketId, expectedProductIds);
        this.basketApiHttpClient.logout();
    }

}