package ru.denisovmaksim.voting;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class VotingApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(VotingApplication.class)
                .profiles(System.getenv().getOrDefault("APP_ENV", "dev"))
                .run(args);
    }

}
