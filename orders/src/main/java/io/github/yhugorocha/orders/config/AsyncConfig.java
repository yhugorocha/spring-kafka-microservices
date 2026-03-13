package io.github.yhugorocha.orders.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean
    public Executor orderValidationExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}
