package com.godaddy.uk.productsapi.models.api.requests;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateProductRequest {

    @NotEmpty(message = "Product name must be not empty.")
    private String name;

    @DecimalMin(value = "0.01", message = "Product price must be greater than 0.")
    private double price;

    @Min(value = 1, message = "Product term must be set to at least 1 month.")
    private int termLengthInMonths;

    @Min(value = 1, message = "Payment interval must be set to at least 1 day.")
    private long paymentIntervalInDays;

    @NotNull(message = "Brand ID must be provided.")
    private int brandId;

    /**
     * Reflection tools constructor.
     */
    public CreateProductRequest() {
    }

    /**
     * Constructor for create product request.
     *
     * @param name                  the name of the product to be created
     * @param price                 the price of the product
     * @param termLengthInMonths    product term length in months
     * @param paymentIntervalInDays payment interval in days
     * @param brandId               id of the brand, where this product is to be offered
     */
    public CreateProductRequest(String name, double price, int termLengthInMonths, long paymentIntervalInDays, int brandId) {
        this.name = name;
        this.price = price;
        this.termLengthInMonths = termLengthInMonths;
        this.paymentIntervalInDays = paymentIntervalInDays;
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getTermLengthInMonths() {
        return termLengthInMonths;
    }

    public long getPaymentIntervalInDays() {
        return paymentIntervalInDays;
    }

    public int getBrandId() {
        return brandId;
    }

}