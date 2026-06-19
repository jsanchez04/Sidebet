package com.sidebet;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WagerRepository extends JpaRepository<Wager, Long> {
    List<Wager> findByMarketId(Long marketId);
}