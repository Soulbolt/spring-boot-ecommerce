package com.code.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "street")
    public String street;

    @Column(name = "city")
    public String city;

    @Column(name = "state")
    public String state;

    @Column(name = "country")
    public String country;

    @Column(name = "zip_code")
    public String zipCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private  Order order;
}
