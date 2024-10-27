package com.cb.client.response;

/**
 * A record representing the response from the inventory service.
 *
 * @param id       the ID of the inventory item
 * @param name     the name of the inventory item
 * @param quantity the quantity of the inventory item
 */
public record InventoryResponse(String id, String name, int quantity) {
}