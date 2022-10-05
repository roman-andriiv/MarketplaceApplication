package com.andriiv.MarketplaceApplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Roman_Andriiv
 */
@Entity
@Table(name = "Products")
public class Product {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @Column(name = "name")
    @NotEmpty(message = "The name of the product is required")
    private String name;

    @Column(name = "price")
    @NotNull(message = "The price of the product is required")
    @Min(value = 0)
    private double price;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userProducts")
    @JsonIgnore
    private Set<User> productOwner;

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<User> getProductOwner() {
        return productOwner;
    }

    public void setProductOwner(Set<User> productOwner) {
        this.productOwner = productOwner;
    }



}
