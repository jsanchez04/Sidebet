package com.sidebet;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {

    private final MarketRepository marketRepository;

    public MarketController(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    @GetMapping("/markets")
    public List<Market> getAllMarkets() {
        return marketRepository.findAll();
    }

    @PostMapping("/markets")
    public Market createMarket(@RequestBody Market market) {
        return marketRepository.save(market);
    }
}