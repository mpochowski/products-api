package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.exceptions.InvalidInputException;
import com.godaddy.uk.productsapi.exceptions.UnauthorisedException;
import com.godaddy.uk.productsapi.models.LoggedInUser;
import com.godaddy.uk.productsapi.models.api.User;
import com.godaddy.uk.productsapi.models.database.BrandEntity;
import com.godaddy.uk.productsapi.models.database.UserEntity;
import com.godaddy.uk.productsapi.repositories.UsersRepository;
import com.godaddy.uk.productsapi.utilities.Loggable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;

import static com.godaddy.uk.productsapi.models.LoggedInUser.USERNAME_BRAND_DELIMITER;
import static com.godaddy.uk.productsapi.models.UserRole.ROLE_USER;
import static com.godaddy.uk.productsapi.transformers.UsersTransformer.toApiModel;
import static java.util.Objects.isNull;

/**
 * <p>
 * Users Service that implements Spring Security {@link UserDetailsService}.
 * The service provides a method to get user by username so that spring can authenticate the user.
 * </p>
 * <p>
 * In Spring context Principal name consist of brand and username separated by {@link LoggedInUser#USERNAME_BRAND_DELIMITER}.
 * This is required to distinguish users between brands they belong to.
 * </p>
 */
@Service
public class UsersService implements UserDetailsService, Loggable {

    @Autowired
    private BrandsService brandsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    /**
     * Implementation of Spring's {@link UserDetailsService} method.
     * In Spring context, application consider authentication name as brand and username separated by {@link LoggedInUser#USERNAME_BRAND_DELIMITER}.
     *
     * @param authenticationName authentication name (brand and username)
     * @return {@link LoggedInUser} that implements Spring's {@link UserDetails} interface
     * @throws UsernameNotFoundException if user was not found or expected authentication name is not in required form
     */
    @Override
    @Transactional
    public LoggedInUser loadUserByUsername(final String authenticationName) throws UsernameNotFoundException {
        String[] brandAndUsername = StringUtils.split(authenticationName, USERNAME_BRAND_DELIMITER);
        if (isNull(brandAndUsername) || brandAndUsername.length != 2) {
            logger().warn("Incorrect brand and username. Expected format 'brand" + USERNAME_BRAND_DELIMITER + "username'.");
            throw new UsernameNotFoundException("User not found by username: " + authenticationName);
        }

        return this.usersRepository
                .findByUsernameAndBrandId(brandAndUsername[1], Integer.parseInt(brandAndUsername[0]))
                .map(user -> new LoggedInUser(user.getBrand().getId(), user.getUsername(), user.getPasswordHash(), user.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + authenticationName));
    }

    /**
     * Registers new user, requires user to do not exist in given brand.
     * We rely on bean validation, but normally some more checks could be added here
     * such as password complexity or requirement of username being different to password, etc.
     *
     * @param brandId  id of the brand the user is to be registered in
     * @param username username that must be unique within brand
     * @param password user password
     * @return created user as api model
     * @throws InvalidInputException if username is already taken in given brand
     */
    @Transactional
    public User registerNewUser(int brandId, @NotNull String username, @NotNull String password) {
        // load brand entity (ensures brand exist)
        BrandEntity brand = brandsService.findById(brandId);

        // make sure there is no user with given username in this brand already
        if (usersRepository.findByUsernameAndBrandId(username, brandId).isPresent()) {
            throw new InvalidInputException("Username '" + username + "' has already been taken.");
        }

        // all good, create new user
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setBrand(brand);
        user.setRole(ROLE_USER);
        user.setSubscriptions(new ArrayList<>());
        return toApiModel(usersRepository.save(user));
    }

    /**
     * Retrieves logged in user entity from {@link Authentication} object.
     * This method returns entity and is intended for internal use only.
     *
     * @param authentication nullable authentication
     * @return user entity
     * @throws UnauthorisedException if user is not logged in
     */
    @Transactional
    public UserEntity getLoggedInUser(@Nullable Authentication authentication) {
        return Optional
                .ofNullable(authentication)
                .filter(UsernamePasswordAuthenticationToken.class::isInstance)
                .map(UsernamePasswordAuthenticationToken.class::cast)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .filter(LoggedInUser.class::isInstance)
                .map(LoggedInUser.class::cast)
                .flatMap(user -> this.usersRepository.findByUsernameAndBrandId(user.getOriginalUsername(), user.getBrandId()))
                .orElseThrow(() -> new UnauthorisedException("User is not authenticated."));
    }

}