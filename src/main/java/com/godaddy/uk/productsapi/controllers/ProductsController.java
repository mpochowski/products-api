package com.godaddy.uk.productsapi.controllers;

import com.godaddy.uk.productsapi.models.api.Product;
import com.godaddy.uk.productsapi.models.api.requests.CreateProductRequest;
import com.godaddy.uk.productsapi.services.ProductsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Product")
@RestController
@RequestMapping(value = "/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @PostMapping
    @ApiOperation("Creates new product, requires admin role.")
    public ResponseEntity<Product> createNewProduct(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.ok(this.productsService.createNewProduct(
                request.getBrandId(),
                request.getName(),
                request.getPrice(),
                request.getTermLengthInMonths(),
                request.getPaymentIntervalInDays()
        ));
    }

}