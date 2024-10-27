package com.cb.model;

/**
 * Record representing an Order.
 *
 * @param orderId  the ID of the order
 * @param itemId   the ID of the item in the order
 * @param itemName the name of the item in the order
 * @param quantity the quantity of the item in the order
 */
public record Order(String orderId, String itemId, String itemName, int quantity) {
}