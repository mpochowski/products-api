package com.godaddy.uk.productsapi.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Model representing logged in user.
 * Implements Spring {@link UserDetails} interface.
 * To be used as authentication principal.
 */
public class LoggedInUser implements UserDetails {

    public static final String USERNAME_BRAND_DELIMITER = "-";

    private final int brandId;
    private final String originalUsername;
    private final String passwordHash;
    private final List<? extends GrantedAuthority> authorities;

    /**
     * Creates new logged in user object.
     *
     * @param brandId          id of the brand user belong to
     * @param originalUsername user username (without brand)
     * @param passwordHash     encrypted password
     * @param userRole         the role user own
     */
    public LoggedInUser(int brandId, String originalUsername, String passwordHash, UserRole userRole) {
        this.brandId = brandId;
        this.originalUsername = originalUsername;
        this.passwordHash = passwordHash;
        this.authorities = List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    /**
     * Returns ID of the Brand this user belongs to.
     *
     * @return int brand id
     */
    public int getBrandId() {
        return brandId;
    }

    /**
     * Returns original username (without brand)
     *
     * @return string username
     */
    public String getOriginalUsername() {
        return originalUsername;
    }

    /**
     * Returns authority username that includes brand id.
     *
     * @return string full username with brand
     */
    @Override
    public String getUsername() {
        return String.join(USERNAME_BRAND_DELIMITER, String.valueOf(this.brandId), this.originalUsername);
    }

    /**
     * Returns password hash.
     *
     * @return password hash
     */
    @Override
    public String getPassword() {
        return passwordHash;
    }

    /**
     * Returns user authorities (user role as list)
     *
     * @return collection of authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}