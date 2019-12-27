package com.godaddy.uk.productsapi.models.api;

public class Brand {

    private int id;
    private String name;

    /**
     * Reflection tools constructor.
     */
    public Brand() {
    }

    /**
     * Creates new Brand.
     *
     * @param id   id of the brand
     * @param name name of the brand
     */
    public Brand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}