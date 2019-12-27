package com.godaddy.uk.productsapi.transformers;

import com.godaddy.uk.productsapi.models.api.Subscription;
import com.godaddy.uk.productsapi.models.database.SubscriptionEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.godaddy.uk.productsapi.utilities.DateTimeUtilities.toEpochSecond;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class SubscriptionsTransformer {

    /**
     * Converts list of entities into API Model objects
     *
     * @param subscriptions nullable list of entities to be converted
     * @return list of {@link Subscription} or null
     */
    public static List<Subscription> toApiModels(@Nullable List<SubscriptionEntity> subscriptions) {
        if (isNull(subscriptions)) {
            return null;
        }

        return subscriptions
                .stream()
                .map(SubscriptionsTransformer::toApiModel)
                .collect(toList());

    }

    /**
     * Converts entity into API Model object
     *
     * @param entity nullable entity to be converted
     * @return instance of {@link Subscription} or null
     */
    public static Subscription toApiModel(@Nullable SubscriptionEntity entity) {
        if (isNull(entity)) {
            return null;
        }

        return new Subscription(
                entity.getId(),
                entity.isActive(),
                toEpochSecond(entity.getExpirationDate()),
                ProductsTransformer.toApiModel(entity.getProduct())
        );
    }

}