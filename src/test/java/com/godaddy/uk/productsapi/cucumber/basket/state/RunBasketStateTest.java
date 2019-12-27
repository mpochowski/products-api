package com.godaddy.uk.productsapi.cucumber.basket.state;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/basket/state",
        plugin = {"pretty", "html:target/feature-basket-state-results"},
        extraGlue = "com.godaddy.uk.productsapi.cucumber.clients"
)
public class RunBasketStateTest {
}