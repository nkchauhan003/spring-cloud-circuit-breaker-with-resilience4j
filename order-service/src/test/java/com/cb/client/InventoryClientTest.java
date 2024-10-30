package com.cb.client;

import com.cb.client.conf.Resilience4JConfiguration;
import com.cb.client.response.InventoryResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Import(value = {Resilience4JConfiguration.class})
@SpringBootTest(classes = InventoryClient.class)
@EnableAspectJAutoProxy
class InventoryClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @MockBean
    private CircuitBreaker circuitBreaker;

    @InjectMocks
    private InventoryClient inventoryClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getInventoryItem_ReturnsItem_WhenCircuitBreakerClosed() {
        InventoryResponse mockResponse = new InventoryResponse("1", "Item 1", 10);
        Mockito.when(restTemplate.getForObject(any(), any())).thenReturn(mockResponse);

        InventoryResponse response = inventoryClient.getInventoryItem("1");

        assertNotNull(response);
        assertEquals("1", response.id());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(InventoryResponse.class));
    }

    @Test
    void getInventoryItem_ReturnsDefault_WhenCircuitBreakerOpen() {
        circuitBreaker.transitionToOpenState();
        InventoryResponse response = inventoryClient.getInventoryItem("1");
        assertNotNull(response);
        assertEquals("default", response.id());
        verify(restTemplate, times(0)).getForObject(anyString(), eq(InventoryResponse.class));
    }

    @Test
    void getInventoryItem_ReturnsItem_WhenCircuitBreakerHalfOpen() {
        circuitBreaker.transitionToHalfOpenState();
        InventoryResponse mockResponse = new InventoryResponse("1", "Item 1", 10);
        when(restTemplate.getForObject(anyString(), eq(InventoryResponse.class))).thenReturn(mockResponse);
        InventoryResponse response = inventoryClient.getInventoryItem("1");
        assertNotNull(response);
        assertEquals("1", response.id());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(InventoryResponse.class));
    }

    @Test
    void getInventoryItem_ReturnsDefault_WhenRestTemplateThrowsException() {
        circuitBreaker.transitionToClosedState();
        when(restTemplate.getForObject(anyString(), eq(InventoryResponse.class))).thenThrow(new RuntimeException("Service unavailable"));
        InventoryResponse response = inventoryClient.getInventoryItem("1");
        assertNotNull(response);
        assertEquals("default", response.id());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(InventoryResponse.class));
    }

/*    @Test
    void getInventoryItem_ReturnsDefault_WhenCircuitBreakerFails() {
        when(circuitBreaker.run(any(), any())).thenThrow(new RuntimeException("Circuit breaker failure"));

        InventoryResponse response = inventoryClient.getInventoryItem("1");

        assertNotNull(response);
        assertEquals("default", response.id());
        verify(restTemplate, times(0)).getForObject(anyString(), eq(InventoryResponse.class));
    }*/
}