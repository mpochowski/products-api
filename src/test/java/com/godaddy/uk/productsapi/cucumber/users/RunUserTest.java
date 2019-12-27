package com.godaddy.uk.productsapi.cucumber.users;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/user",
        plugin = {"pretty", "html:target/feature-user-results"},
        extraGlue = "com.godaddy.uk.productsapi.cucumber.clients"
)
public class RunUserTest {
}