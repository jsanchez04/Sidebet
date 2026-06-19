package com.sidebet;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BettingService {

    private final UserRepository userRepository;
    private final MarketRepository marketRepository;
    private final WagerRepository wagerRepository;

    public BettingService(UserRepository userRepository,
                          MarketRepository marketRepository,
                          WagerRepository wagerRepository) {
        this.userRepository = userRepository;
        this.marketRepository = marketRepository;
        this.wagerRepository = wagerRepository;
    }

    @Transactional
    public Wager placeWager(Wager wager) {
        Market market = marketRepository.findById(wager.getMarketId())
                .orElseThrow(() -> new IllegalArgumentException("Market not found"));
        if (!market.getStatus().equals("OPEN")) {
            throw new IllegalStateException("Market is already closed");
        }
        String side = wager.getSide().toUpperCase();
        if (!side.equals("YES") && !side.equals("NO")) {
            throw new IllegalArgumentException("Side must be YES or NO");
        }
        if (wager.getStake() <= 0) {
            throw new IllegalArgumentException("Stake must be positive");
        }
        User user = userRepository.findById(wager.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getPointsBalance() < wager.getStake()) {
            throw new IllegalStateException("Not enough points");
        }

        // Take the stake out of their balance now (held until the market resolves)
        user.setPointsBalance(user.getPointsBalance() - wager.getStake());
        userRepository.save(user);

        wager.setSide(side);
        return wagerRepository.save(wager);
    }

    @Transactional
    public Market resolveMarket(Long marketId, String outcome) {
        String result = outcome.toUpperCase();
        if (!result.equals("YES") && !result.equals("NO")) {
            throw new IllegalArgumentException("Outcome must be YES or NO");
        }
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new IllegalArgumentException("Market not found"));
        if (!market.getStatus().equals("OPEN")) {
            throw new IllegalStateException("Market is already resolved");
        }

        List<Wager> wagers = wagerRepository.findByMarketId(marketId);

        int totalPool = 0;
        int winningStake = 0;
        for (Wager w : wagers) {
            totalPool += w.getStake();
            if (w.getSide().equals(result)) {
                winningStake += w.getStake();
            }
        }

        if (winningStake == 0) {
            // Nobody bet the winning side — refund everyone's stake
            for (Wager w : wagers) {
                User u = userRepository.findById(w.getUserId()).orElseThrow();
                u.setPointsBalance(u.getPointsBalance() + w.getStake());
                userRepository.save(u);
            }
        } else {
            // Split the whole pool among winners, in proportion to what they staked
            for (Wager w : wagers) {
                if (w.getSide().equals(result)) {
                    int payout = (int) ((long) totalPool * w.getStake() / winningStake);
                    User u = userRepository.findById(w.getUserId()).orElseThrow();
                    u.setPointsBalance(u.getPointsBalance() + payout);
                    userRepository.save(u);
                }
            }
        }

        market.setStatus(result);
        return marketRepository.save(market);
    }
}