package com.sidebet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Wager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long marketId;
    private Long userId;
    private String side;   // "YES" or "NO"
    private int stake;

    protected Wager() {}

    public Wager(Long marketId, Long userId, String side, int stake) {
        this.marketId = marketId;
        this.userId = userId;
        this.side = side;
        this.stake = stake;
    }

    public Long getId() { return id; }
    public Long getMarketId() { return marketId; }
    public Long getUserId() { return userId; }
    public String getSide() { return side; }
    public void setSide(String side) { this.side = side; }
    public int getStake() { return stake; }
}