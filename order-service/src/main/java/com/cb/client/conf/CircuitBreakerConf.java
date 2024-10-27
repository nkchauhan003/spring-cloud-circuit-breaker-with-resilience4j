package com.cb.client.conf;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties("inventory.circuit-breaker")
@Setter
@Slf4j
public class CircuitBreakerConf {
    private String name;
    private Integer slidingWindowSize;
    private Float failureRateThreshold;
    private Integer waitDurationInOpenStateMilis;
    private Integer permittedNumberOfCallsInHalfOpenState;
    private Float slowCallRateThreshold;
    private Integer slowCallDurationThresholdMilis;

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        // Custom configuration
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(slidingWindowSize)
                .failureRateThreshold(failureRateThreshold)
                .waitDurationInOpenState(Duration.ofMillis(waitDurationInOpenStateMilis))
                .permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slowCallRateThreshold(slowCallRateThreshold)
                .slowCallDurationThreshold(Duration.ofMillis(slowCallDurationThresholdMilis))
                .build();
        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public CircuitBreaker inventoryClientCb(CircuitBreakerRegistry circuitBreakerRegistry) {
        var circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);
        circuitBreaker.getEventPublisher()
                .onStateTransition(this::onStateTransition);
        return circuitBreaker;
    }

    private void onStateTransition(CircuitBreakerOnStateTransitionEvent event) {
        log.error("CircuitBreaker '{}' changed state from {} to {}", event.getCircuitBreakerName(), event.getStateTransition().getFromState(), event.getStateTransition().getToState());
    }

}
