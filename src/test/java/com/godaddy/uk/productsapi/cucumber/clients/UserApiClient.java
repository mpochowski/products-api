package com.godaddy.uk.productsapi.cucumber.clients;

import com.godaddy.uk.productsapi.models.api.User;
import com.godaddy.uk.productsapi.models.api.requests.RegisterUserRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class UserApiClient extends ApiClient {

    private static final String API_PATH_USERS = "/users";
    private static final String API_PATH_CURRENT_USER = "/~";
    private static final ParameterizedTypeReference<User> USER_TYPE = new ParameterizedTypeReference<>() {
    };

    public User registerUser(int brandId, String username, String password) {
        return doHttpRequest(
                POST,
                this.buildServerUrl(API_PATH_USERS),
                new RegisterUserRequest(brandId, username, password),
                USER_TYPE
        );
    }

    public User currentUserDetails() {
        return doHttpRequest(
                GET,
                this.buildServerUrl(API_PATH_USERS + API_PATH_CURRENT_USER),
                null,
                USER_TYPE
        );
    }

}