package com.sidebet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MarketRepository marketRepository;

    public DataSeeder(UserRepository userRepository, MarketRepository marketRepository) {
        this.userRepository = userRepository;
        this.marketRepository = marketRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Already seeded — don't add duplicates
        }
        userRepository.save(new User("marco", 1000));
        userRepository.save(new User("jeremiah", 1000));
        userRepository.save(new User("priya", 1000));

        marketRepository.save(new Market("Will Marco be on time Friday?", 1L));
    }
}