package com.godaddy.uk.productsapi.models.api;

public class Subscription {

    private int id;
    private boolean active;
    private long expirationDateEpoch;
    private Product product;

    /**
     * Reflection tools constructor.
     */
    public Subscription() {
    }

    /**
     * Creates new subscription object.
     *
     * @param id                  id of the subscription
     * @param active              active flag
     * @param expirationDateEpoch expiration date as epoch time
     * @param product             the subscription product
     */
    public Subscription(int id, boolean active, long expirationDateEpoch, Product product) {
        this.id = id;
        this.active = active;
        this.expirationDateEpoch = expirationDateEpoch;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public long getExpirationDateEpoch() {
        return expirationDateEpoch;
    }

    public Product getProduct() {
        return product;
    }

}