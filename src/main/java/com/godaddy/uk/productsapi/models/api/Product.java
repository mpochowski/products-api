package com.godaddy.uk.productsapi.models.api;

public class Product {

    private int id;
    private String name;
    private double price;
    private int termLengthInMonths;
    private long paymentIntervalInDays;

    /**
     * Reflection tools constructor.
     */
    public Product() {
    }

    /**
     * Creates new product.
     *
     * @param id                    id of the product
     * @param name                  name of the product
     * @param price                 product price
     * @param termLengthInMonths    the length of the product contract in months
     * @param paymentIntervalInDays interval between payments in days
     */
    public Product(int id, String name, double price, int termLengthInMonths, long paymentIntervalInDays) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.termLengthInMonths = termLengthInMonths;
        this.paymentIntervalInDays = paymentIntervalInDays;
    }

    public int getId() {
        return id;
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

}