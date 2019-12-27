package com.godaddy.uk.productsapi.models.api.requests;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateBasketRequest {

    @NotNull(message = "Initial products must not be null.")
    private List<Integer> initialProductIds;

    /**
     * Reflection tools constructor.
     */
    public CreateBasketRequest() {
    }

    /**
     * Creates new create basket request.
     *
     * @param initialProductIds initial products list
     */
    public CreateBasketRequest(List<Integer> initialProductIds) {
        this.initialProductIds = initialProductIds;
    }

    public List<Integer> getInitialProductIds() {
        return initialProductIds;
    }

}