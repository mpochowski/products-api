package com.godaddy.uk.productsapi.cucumber.clients;

import com.godaddy.uk.productsapi.models.BasketState;
import com.godaddy.uk.productsapi.models.api.Basket;
import com.godaddy.uk.productsapi.models.api.requests.CreateBasketRequest;
import com.godaddy.uk.productsapi.models.api.requests.UpdateBasketStateRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.springframework.http.HttpMethod.*;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class BasketApiClient extends ApiClient {

    private static final String API_PATH_BASKETS = "/baskets";
    private static final String API_PATH_PRODUCTS = "/products";
    private static final String API_PATH_STATE = "/state";
    private static final ParameterizedTypeReference<Basket> BASKET_TYPE = new ParameterizedTypeReference<>() {
    };

    public Basket createBasket(CreateBasketRequest request) {
        return doHttpRequest(
                POST,
                this.buildServerUrl(API_PATH_BASKETS),
                request,
                BASKET_TYPE
        );
    }

    public Basket getBasketById(int basketId) {
        return doHttpRequest(
                GET,
                this.buildServerUrl(API_PATH_BASKETS + "/" + basketId),
                null,
                BASKET_TYPE
        );
    }

    public Basket addBasketProducts(int basketId, List<Integer> productIds) {
        return doHttpRequest(
                POST,
                this.buildServerUrl(API_PATH_BASKETS + "/" + basketId + API_PATH_PRODUCTS),
                productIds,
                BASKET_TYPE
        );
    }

    public Basket removeProducts(int basketId, List<Integer> productIds) {
        return doHttpRequest(
                DELETE,
                this.buildServerUrl(API_PATH_BASKETS + "/" + basketId + API_PATH_PRODUCTS),
                productIds,
                BASKET_TYPE
        );
    }

    public Basket updateBasketState(int basketId, BasketState newState) {
        return doHttpRequest(
                PUT,
                this.buildServerUrl(API_PATH_BASKETS + "/" + basketId + API_PATH_STATE),
                new UpdateBasketStateRequest(newState),
                BASKET_TYPE
        );
    }

}