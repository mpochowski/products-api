package com.godaddy.uk.productsapi.models.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "brand")
public class BrandEntity {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}