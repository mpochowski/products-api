package com.godaddy.uk.productsapi.transformers;

import com.godaddy.uk.productsapi.models.api.User;
import com.godaddy.uk.productsapi.models.database.UserEntity;

import static java.util.Objects.isNull;

public class UsersTransformer {

    /**
     * Converts entity into API Model object
     *
     * @param entity nullable entity to be converted
     * @return instance of {@link User} or null
     */
    public static User toApiModel(UserEntity entity) {
        if (isNull(entity)) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getRole(),
                SubscriptionsTransformer.toApiModels(entity.getSubscriptions())
        );
    }

}