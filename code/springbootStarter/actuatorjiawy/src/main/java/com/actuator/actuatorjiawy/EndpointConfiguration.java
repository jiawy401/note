package com.actuator.actuatorjiawy;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfiguration {

    @Bean
    public CustomerMetricsIndicator customerHealthIndicator(){
        return new CustomerMetricsIndicator();
    }
}
