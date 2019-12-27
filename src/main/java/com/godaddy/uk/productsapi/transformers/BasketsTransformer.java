package com.godaddy.uk.productsapi.transformers;

import com.godaddy.uk.productsapi.models.api.Basket;
import com.godaddy.uk.productsapi.models.database.BasketEntity;
import com.godaddy.uk.productsapi.models.database.ProductEntity;
import org.jetbrains.annotations.Nullable;

import static com.godaddy.uk.productsapi.utilities.MoneyUtilities.sumMoneyValues;
import static java.util.Objects.isNull;

public class BasketsTransformer {

    /**
     * Converts entity into API Model object
     *
     * @param entity nullable entity to be converted
     * @return instance of {@link Basket} or null
     */
    public static Basket toApiModel(@Nullable BasketEntity entity) {
        if (isNull(entity)) {
            return null;
        }

        // find basket value
        double basketValue = sumMoneyValues(entity.getProducts(), ProductEntity::getPrice);

        return new Basket(
                entity.getId(),
                entity.getBasketState(),
                ProductsTransformer.toApiModels(entity.getProducts()),
                basketValue
        );
    }

}
