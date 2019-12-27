package com.godaddy.uk.productsapi.models.database;

import javax.persistence.*;
import java.time.Duration;
import java.time.Period;

@Entity(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 2)")
    private double price;

    @Column(nullable = false)
    private Period termLength;

    @Column(nullable = false)
    private Duration paymentInterval;

    @ManyToOne
    private BrandEntity brand;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Period getTermLength() {
        return termLength;
    }

    public void setTermLength(Period termLength) {
        this.termLength = termLength;
    }

    public Duration getPaymentInterval() {
        return paymentInterval;
    }

    public void setPaymentInterval(Duration paymentInterval) {
        this.paymentInterval = paymentInterval;
    }

    public BrandEntity getBrand() {
        return brand;
    }

    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }

}