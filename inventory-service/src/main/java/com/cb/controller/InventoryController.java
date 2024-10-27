package com.cb.controller;

import com.cb.model.Item;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * InventoryController is a REST controller that handles requests related to inventory items.
 */
@RestController
public class InventoryController {

    /**
     * Handles GET requests to /inventory/{itemId}.
     *
     * @param itemId the ID of the item to retrieve
     * @return a dummy Item object with the specified itemId
     */
    @GetMapping("/inventory/{itemId}")
    public Item getItem(@PathVariable String itemId) {
        if (true) {
            throw new RuntimeException("Service is unavailable");
        }
        return new Item(itemId, "Dummy Item", 10);
    }
}