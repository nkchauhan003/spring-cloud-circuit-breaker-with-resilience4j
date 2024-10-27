package com.cb.controller;

import com.cb.client.InventoryClient;
import com.cb.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling order-related requests.
 */
@RestController
public class OrderController {

    @Autowired
    private InventoryClient inventoryClient;

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the Order object containing order details
     */
    @GetMapping("/order/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        // Dummy itemId for demonstration purposes
        var itemId = "dummyItemId";
        var inventoryResponse = inventoryClient.getInventoryItem(itemId);
        // Create a dummy Order Response using the inventory data
        var orderResponse = new Order(orderId, itemId, inventoryResponse.name(), inventoryResponse.quantity());
        return orderResponse;
    }
}