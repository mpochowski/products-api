package com.godaddy.uk.productsapi.controllers;

import com.godaddy.uk.productsapi.models.api.User;
import com.godaddy.uk.productsapi.models.api.requests.RegisterUserRequest;
import com.godaddy.uk.productsapi.services.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.godaddy.uk.productsapi.transformers.UsersTransformer.toApiModel;

@Api(tags = "User")
@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping
    @ApiOperation("Registers new user.")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegisterUserRequest request) {
        return ResponseEntity.ok(this.usersService.registerNewUser(request.getBrandId(), request.getUsername(), request.getPassword()));
    }

    @GetMapping("/~")
    @ApiOperation("Retrieves currently logged in user details.")
    public ResponseEntity<User> currentUserDetails(@NotNull Authentication authentication) {
        return ResponseEntity.ok(toApiModel(this.usersService.getLoggedInUser(authentication)));
    }

}