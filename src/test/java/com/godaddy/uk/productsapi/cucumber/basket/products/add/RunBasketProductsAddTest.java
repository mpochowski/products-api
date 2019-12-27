package com.godaddy.uk.productsapi.cucumber.basket.products.add;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/basket/products-add",
        plugin = {"pretty", "html:target/feature-basket-add-results"},
        extraGlue = "com.godaddy.uk.productsapi.cucumber.clients"
)
public class RunBasketProductsAddTest {
}