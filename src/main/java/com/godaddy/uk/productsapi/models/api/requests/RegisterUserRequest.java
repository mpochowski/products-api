package com.godaddy.uk.productsapi.models.api.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterUserRequest {

    @NotNull(message = "Brand ID must be provided.")
    private int brandId;

    @Size(min = 3, message = "Username must be at least 3 characters long.")
    private String username;

    @Size(min = 3, message = "Password must be at least 3 characters long.")
    private String password;

    /**
     * Reflection tools constructor.
     */
    public RegisterUserRequest() {
    }

    /**
     * Creates new register user request.
     *
     * @param brandId  id of the brand the user belong to
     * @param username unique username
     * @param password user password
     */
    public RegisterUserRequest(int brandId, String username, String password) {
        this.brandId = brandId;
        this.username = username;
        this.password = password;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}