package com.foodorder.kitchenservice.model;

import java.io.Serializable;
import java.time.Instant;

/** Published to "order-ready" once the kitchen finishes preparing an order. */
public class OrderReadyEvent implements Serializable {
    private String orderId;
    private String restaurantId;
    private String deliveryAddress;
    private Instant readyAt;

    public OrderReadyEvent() {}

    public OrderReadyEvent(String orderId, String restaurantId, String deliveryAddress, Instant readyAt) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.deliveryAddress = deliveryAddress;
        this.readyAt = readyAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public Instant getReadyAt() { return readyAt; }
    public void setReadyAt(Instant readyAt) { this.readyAt = readyAt; }
}
