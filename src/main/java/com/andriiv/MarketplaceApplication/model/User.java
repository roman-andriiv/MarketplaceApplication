package com.andriiv.MarketplaceApplication.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author Roman_Andriiv
 */
@Entity
@Table(name = "Users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @NotEmpty
    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, message = "The first name should  have at least 2 characters")
    private String firstName;

    @NotEmpty
    @Column(name = "last_name")
    @NotNull
    @Size(min = 2, message = " The last name name should  have at least 2 characters")
    private String lastName;

    @Column(name = "amount_of_money")
    @NotNull
    @Min(value = 0)
    private double amountOfMoney;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_table_products",
            joinColumns = { @JoinColumn(name = "user_id")},
            inverseJoinColumns = { @JoinColumn (name = "product_id")})
    private Set<Product> userProducts;

    public User() {
    }

    public User(String firstName, String lastName, double amountOfMoney) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.amountOfMoney = amountOfMoney;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public Set<Product> getUserProducts() {
        return userProducts;
    }

    public void setUserProducts(Set<Product> userProducts) {
        this.userProducts = userProducts;
    }
}
