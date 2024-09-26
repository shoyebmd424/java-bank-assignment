package com.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountType;
    private double balance;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
    public Account(String accountType) {
        this.accountType = accountType;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public void credit(double amount) {
        this.balance += amount;
        transactions.add(new Transaction("Credit", amount, 0));
    }

    public void debit(double amount, double fee) {
        this.balance -= (amount + fee);
        transactions.add(new Transaction("Debit", amount, fee));
    }

}
