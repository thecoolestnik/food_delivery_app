package com.foodorder.deliveryboyservice.model;

import java.io.Serializable;
import java.time.Instant;

public class OrderReadyEvent implements Serializable {
    private String orderId;
    private String restaurantId;
    private String deliveryAddress;
    private Instant readyAt;

    public OrderReadyEvent() {}

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public Instant getReadyAt() { return readyAt; }
    public void setReadyAt(Instant readyAt) { this.readyAt = readyAt; }
}
