package com.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Account currentAccount;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Account savingAccount;

    public Customer(String name) {
        this.name = name;
        this.currentAccount = new Account("Current");
        this.savingAccount = new Account("Savings");
        this.savingAccount.credit(500); // Join bonus of R500
    }

}
