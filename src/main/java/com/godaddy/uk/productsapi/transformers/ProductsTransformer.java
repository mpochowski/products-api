package com.godaddy.uk.productsapi.transformers;

import com.godaddy.uk.productsapi.models.api.Product;
import com.godaddy.uk.productsapi.models.database.ProductEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class ProductsTransformer {

    /**
     * Converts list of entities into API Model objects
     *
     * @param entities nullable list of entities to be converted
     * @return list of {@link Product} or null
     */
    public static List<Product> toApiModels(@Nullable List<ProductEntity> entities) {
        if (isNull(entities)) {
            return null;
        }

        return entities
                .stream()
                .map(ProductsTransformer::toApiModel)
                .collect(toList());
    }

    /**
     * Converts entity into API Model object
     *
     * @param entity nullable entity to be converted
     * @return instance of {@link Product} or null
     */
    public static Product toApiModel(@Nullable ProductEntity entity) {
        if (isNull(entity)) {
            return null;
        }

        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getTermLength().getMonths(),
                entity.getPaymentInterval().toDays()
        );
    }

}