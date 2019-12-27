package com.godaddy.uk.productsapi.models.api.requests;

import com.godaddy.uk.productsapi.models.BasketState;

import javax.validation.constraints.NotNull;

public class UpdateBasketStateRequest {

    @NotNull(message = "New basket state must be provided.")
    private BasketState newState;

    /**
     * Reflection tools constructor.
     */
    public UpdateBasketStateRequest() {
    }

    /**
     * Creates new {@link UpdateBasketStateRequest} object.
     *
     * @param newState not null, new basket state
     */
    public UpdateBasketStateRequest(@NotNull BasketState newState) {
        this.newState = newState;
    }

    public BasketState getNewState() {
        return newState;
    }

}