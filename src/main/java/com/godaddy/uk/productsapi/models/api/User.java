package com.godaddy.uk.productsapi.models.api;

import com.godaddy.uk.productsapi.models.UserRole;

import java.util.List;

public class User {

    private int id;
    private String username;
    private UserRole role;
    private List<Subscription> subscriptions;

    /**
     * Reflection tools constructor.
     */
    public User() {
    }

    /**
     * Creates new user.
     *
     * @param id            of the user
     * @param username      unique username
     * @param role          the role of this user in the platform
     * @param subscriptions list of user subscriptions
     */
    public User(int id, String username, UserRole role, List<Subscription> subscriptions) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.subscriptions = subscriptions;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

}