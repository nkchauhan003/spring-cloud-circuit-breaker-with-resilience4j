package com.cb.client;

import com.cb.client.response.InventoryResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.SupplierUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

/**
 * Client for interacting with the inventory service.
 */
@Component
public class InventoryClient {

    private static final Logger logger = LoggerFactory.getLogger(InventoryClient.class);

    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;
    private final CircuitBreaker circuitBreaker;

    public InventoryClient(RestTemplate restTemplate, @Value("${inventory.service.url}") String inventoryServiceUrl, CircuitBreaker circuitBreaker) {
        this.restTemplate = restTemplate;
        this.inventoryServiceUrl = inventoryServiceUrl;
        this.circuitBreaker = circuitBreaker;
    }


    /**
     * Fetches an inventory item by its ID, with circuit breaker support.
     *
     * @param itemId the ID of the inventory item to fetch
     * @return the fetched InventoryResponse, or null if the request fails
     */
    public InventoryResponse getInventoryItem(String itemId) {
        String url = inventoryServiceUrl + itemId;
        logger.info("Fetching inventory item from URL: {}", url);
        Supplier<InventoryResponse> supplier = () -> {
            // This code is executed within the circuit breaker
            var response = restTemplate.getForObject(url, InventoryResponse.class);
            logger.info("Received inventory item: {}", response);
            return response;
        };
        Supplier<InventoryResponse> supplierWithRecovery = SupplierUtils
                .recover(supplier, exception -> fallbackGetInventoryItem(itemId, exception));
        return circuitBreaker.executeSupplier(supplierWithRecovery);
    }

    /**
     * Fallback method for getInventoryItem, called when the circuit breaker is open or an error occurs.
     *
     * @param itemId the ID of the inventory item that was attempted to be fetched
     * @param t      the throwable that caused the fallback
     * @return null, or a default InventoryResponse
     */
    public InventoryResponse fallbackGetInventoryItem(String itemId, Throwable t) {
        logger.error("Error fetching inventory for itemId: {}, so falling back to default response", itemId, t);
        // or you can return a default InventoryResponse
        return new InventoryResponse("default", "Default Item", 0);
    }
}