package com.sidebet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = " app user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int pointsBalance;

    protected User() {} // JPA requires an empty constructor

    public User(String username, int pointsBalance) {
        this.username = username;
        this.pointsBalance = pointsBalance;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public int getPointsBalance() { return pointsBalance; }
    public void setPointsBalance(int pointsBalance) { this.pointsBalance = pointsBalance; }
}