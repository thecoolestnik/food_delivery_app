package com.foodorder.orderservice.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * Event published to the "order-placed" Kafka topic when a customer places an order.
 * Consumed downstream by kitchen-service.
 */
public class OrderEvent implements Serializable {

    private String orderId;
    private String customerId;
    private String restaurantId;
    private List<String> items;
    private double totalAmount;
    private String deliveryAddress;
    private Instant placedAt;

    public OrderEvent() {
    }

    public OrderEvent(String orderId, String customerId, String restaurantId,
                       List<String> items, double totalAmount, String deliveryAddress, Instant placedAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;
        this.placedAt = placedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public Instant getPlacedAt() { return placedAt; }
    public void setPlacedAt(Instant placedAt) { this.placedAt = placedAt; }

    @Override
    public String toString() {
        return "OrderEvent{orderId='" + orderId + "', customerId='" + customerId +
                "', restaurantId='" + restaurantId + "', items=" + items +
                ", totalAmount=" + totalAmount + ", deliveryAddress='" + deliveryAddress +
                "', placedAt=" + placedAt + '}';
    }
}
