package com.godaddy.uk.productsapi.repositories;

import com.godaddy.uk.productsapi.models.database.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Retrieves user by username and brand id.
     *
     * @param username username used to register user
     * @param brandId  id of the brand the user was registered in
     * @return optional of user entity
     */
    Optional<UserEntity> findByUsernameAndBrandId(String username, int brandId);

}