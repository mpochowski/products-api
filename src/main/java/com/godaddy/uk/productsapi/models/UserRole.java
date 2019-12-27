package com.godaddy.uk.productsapi.models;

/**
 * Simple user role enum, in complex applications this could be broken down into role and permissions system.
 */
public enum UserRole {

    /**
     * Default user role with basic permissions.
     */
    ROLE_USER,

    /**
     * Admin user role, allowing platform administration.
     */
    ROLE_ADMIN

}