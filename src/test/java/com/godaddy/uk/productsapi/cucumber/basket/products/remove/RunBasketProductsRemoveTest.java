package com.godaddy.uk.productsapi.cucumber.basket.products.remove;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/basket/products-remove",
        plugin = {"pretty", "html:target/feature-basket-remove-results"},
        extraGlue = "com.godaddy.uk.productsapi.cucumber.clients"
)
public class RunBasketProductsRemoveTest {
}