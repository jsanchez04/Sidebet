package com.sidebet;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {

    private final MarketRepository marketRepository;
    private final WagerRepository wagerRepository;

    public MarketController(MarketRepository marketRepository, WagerRepository wagerRepository) {
        this.marketRepository = marketRepository;
        this.wagerRepository = wagerRepository;
    }

    @GetMapping("/markets")
    public List<Market> getAllMarkets() {
        return marketRepository.findAll();
    }

    @PostMapping("/markets")
    public Market createMarket(@RequestBody Market market) {
        return marketRepository.save(market);
    }

    @GetMapping("/markets/{id}/stats")
    public Map<String, Integer> getMarketStats(@PathVariable Long id) {
        List<Wager> wagers = wagerRepository.findByMarketId(id);
        int yes = 0, no = 0;
        for (Wager w : wagers) {
            if (w.getSide().equals("YES")) yes += w.getStake();
            else if (w.getSide().equals("NO")) no += w.getStake();
        }
        return Map.of("yesStake", yes, "noStake", no, "totalStake", yes + no);
    }
}