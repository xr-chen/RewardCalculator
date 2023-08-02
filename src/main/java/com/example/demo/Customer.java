package com.example.demo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    private String accountName;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Purchase> purchases = new ArrayList<>();
}
