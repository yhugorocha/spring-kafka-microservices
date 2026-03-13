package io.github.yhugorocha.orders.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "io.github.yhugorocha.orders.client")
public class ClientsConfig {
}
