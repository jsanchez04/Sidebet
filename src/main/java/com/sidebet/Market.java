package com.sidebet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "market")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;       // "Will Marco be on time Friday?"
    private Long creatorId;        // which user made it
    private String status = "OPEN"; // OPEN, then YES or NO once resolved

    protected Market() {}

    public Market(String question, Long creatorId) {
        this.question = question;
        this.creatorId = creatorId;
    }

    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public Long getCreatorId() { return creatorId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}