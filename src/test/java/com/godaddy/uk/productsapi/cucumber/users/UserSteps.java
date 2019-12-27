package com.godaddy.uk.productsapi.cucumber.users;

import com.godaddy.uk.productsapi.cucumber.clients.UserApiClient;
import com.godaddy.uk.productsapi.models.api.User;
import com.godaddy.uk.productsapi.utilities.Loggable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSteps implements Loggable {

    private UserApiClient userApiHttpClient;

    private int brandId;
    private User user;

    @Autowired
    public UserSteps(UserApiClient userApiHttpClient) {
        this.userApiHttpClient = userApiHttpClient;
    }

    @Given("I'm using brand with id {int}.")
    public void i_m_using_brand_with_id(int brandId) {
        this.brandId = brandId;
    }

    @Given("I'm logged in as {string} using password {string}.")
    public void i_m_logged_in_as_using_password(String username, String password) {
        this.userApiHttpClient.login(this.brandId, username, password);
    }

    @Given("I'm not logged in.")
    public void i_m_not_logged_in() {
        this.userApiHttpClient.logout();
    }

    @When("I ask to register user with username {string} and password {string}.")
    public void i_ask_to_register_user_with_login_and_password(String login, String password) {
        this.user = this.userApiHttpClient.registerUser(this.brandId, login, password);
    }

    @When("I ask to get my user details.")
    public void i_ask_to_get_my_user_details() {
        this.user = this.userApiHttpClient.currentUserDetails();
    }

    @Then("The user should be successfully registered with username {string}.")
    public void the_user_should_be_successfully_registered(String expectedUsername) {
        assertNotNull(this.user);
        assertEquals(expectedUsername, this.user.getUsername());
        this.userApiHttpClient.logout();
    }

    @Then("The user should be returned with username {string}.")
    public void the_user_should_be_returned_with_username(String expectedUsername) {
        assertNotNull(this.user);
        assertEquals(expectedUsername, this.user.getUsername());
        this.userApiHttpClient.logout();
    }

    @Then("The user should not be registered.")
    public void the_user_should_not_be_registered() {
        assertNull(this.user);
    }

    @Then("There should be no user details returned.")
    public void there_should_be_no_user_details_returned() {
        assertNull(this.user);
    }

}