package com.godaddy.uk.productsapi.models.api;

import com.godaddy.uk.productsapi.models.BasketState;

import java.util.List;

public class Basket {

    private int id;
    private BasketState state;
    private List<Product> products;
    private double basketValue;

    /**
     * Reflection tools constructor.
     */
    public Basket() {
    }

    /**
     * Creates new basket.
     *
     * @param id          id of the basket
     * @param state       state of the basket
     * @param products    list of products in basket
     * @param basketValue the value of the basket
     */
    public Basket(int id, BasketState state, List<Product> products, double basketValue) {
        this.id = id;
        this.state = state;
        this.products = products;
        this.basketValue = basketValue;
    }

    public int getId() {
        return id;
    }

    public BasketState getState() {
        return state;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getBasketValue() {
        return basketValue;
    }

}