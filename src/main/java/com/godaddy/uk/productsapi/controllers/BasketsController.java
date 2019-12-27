package com.godaddy.uk.productsapi.controllers;

import com.godaddy.uk.productsapi.models.api.Basket;
import com.godaddy.uk.productsapi.models.api.requests.CreateBasketRequest;
import com.godaddy.uk.productsapi.models.api.requests.UpdateBasketStateRequest;
import com.godaddy.uk.productsapi.services.BasketsService;
import com.godaddy.uk.productsapi.services.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Basket")
@RestController
@RequestMapping(value = "/baskets")
public class BasketsController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private BasketsService basketService;

    @PostMapping
    @ApiOperation("Creates a new basket for given brand, user and with initial products.")
    public ResponseEntity<Basket> createBasket(@RequestBody @Valid CreateBasketRequest request, @NotNull Authentication authentication) {
        return ResponseEntity.ok(this.basketService.createBasket(
                this.usersService.getLoggedInUser(authentication),
                request.getInitialProductIds()
        ));
    }

    @GetMapping("/{basketId}")
    @ApiOperation("Retrieves basket by id.")
    public ResponseEntity<Basket> getBasketById(@PathVariable int basketId, @NotNull Authentication authentication) {
        return ResponseEntity.ok(this.basketService.getBasketById(
                this.usersService.getLoggedInUser(authentication),
                basketId
        ));
    }

    @PutMapping("/{basketId}/state")
    @ApiOperation("Updates basket state.")
    public ResponseEntity<Basket> updateBasketState(@PathVariable int basketId,
                                                    @RequestBody @Valid UpdateBasketStateRequest request,
                                                    @NotNull Authentication authentication) {
        return ResponseEntity.ok(this.basketService.updateBasketState(
                this.usersService.getLoggedInUser(authentication),
                basketId,
                request.getNewState()
        ));
    }

    @PostMapping("/{basketId}/products")
    @ApiOperation("Adds new products to given basket.")
    public ResponseEntity<Basket> addProducts(@PathVariable int basketId,
                                              @RequestBody List<Integer> newProductIds,
                                              @NotNull Authentication authentication) {
        return ResponseEntity.ok(this.basketService.addProducts(
                this.usersService.getLoggedInUser(authentication),
                basketId,
                newProductIds
        ));
    }

    @DeleteMapping("/{basketId}/products")
    @ApiOperation("Deletes products from given basket.")
    public ResponseEntity<Basket> removeProducts(@PathVariable int basketId,
                                                 @RequestBody List<Integer> productIds,
                                                 @NotNull Authentication authentication) {
        return ResponseEntity.ok(this.basketService.removeProducts(
                this.usersService.getLoggedInUser(authentication),
                basketId,
                productIds
        ));
    }

}