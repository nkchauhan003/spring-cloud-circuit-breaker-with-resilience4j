package com.cb.model;

/**
 * Represents an item in the inventory.
 *
 * @param id the unique identifier of the item
 * @param name the name of the item
 * @param quantity the quantity of the item in stock
 */
public record Item(String id, String name, int quantity) {
}