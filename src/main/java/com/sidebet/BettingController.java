package com.sidebet;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BettingController {

    private final BettingService bettingService;
    private final WagerRepository wagerRepository;

    public BettingController(BettingService bettingService, WagerRepository wagerRepository) {
        this.bettingService = bettingService;
        this.wagerRepository = wagerRepository;
    }

    @GetMapping("/wagers")
    public List<Wager> getAllWagers() {
        return wagerRepository.findAll();
    }

    @PostMapping("/wagers")
    public Wager placeWager(@RequestBody Wager wager) {
        return bettingService.placeWager(wager);
    }

    @PostMapping("/markets/{marketId}/resolve")
    public Market resolveMarket(@PathVariable Long marketId, @RequestParam String outcome) {
        return bettingService.resolveMarket(marketId, outcome);
    }
}