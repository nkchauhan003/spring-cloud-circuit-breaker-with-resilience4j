package com.cb.client.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for client-related beans.
 */
@Configuration
public class ClientConfig {

    /**
     * Creates a {@link RestTemplate} bean.
     *
     * @return a new instance of {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}