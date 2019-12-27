package com.godaddy.uk.productsapi.models.database;

import com.godaddy.uk.productsapi.models.BasketState;

import javax.persistence.*;
import java.util.List;

@Entity(name = "basket")
public class BasketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BasketState basketState;

    @ManyToOne
    private UserEntity owner;

    @ManyToMany
    private List<ProductEntity> products;

    public int getId() {
        return id;
    }

    public BasketState getBasketState() {
        return basketState;
    }

    public void setBasketState(BasketState basketState) {
        this.basketState = basketState;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

}