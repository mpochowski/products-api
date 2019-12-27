package com.godaddy.uk.productsapi.models;

/**
 * Defines basket states.
 */
public enum BasketState {

    /**
     * User is shopping state, so they can freely add and remove products in basket.
     */
    IN_PROCESS,

    /**
     * Means user is about to do payment, they cannot modify basket state.
     * This is transition state, user can get back to {@link BasketState#IN_PROCESS} or complete payment and get to {@link BasketState#COMPLETED} state.
     */
    IN_CHECKOUT,

    /**
     * User has completed the payment, basket cannot be modified anymore.
     */
    COMPLETED

}