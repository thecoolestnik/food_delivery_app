package com.foodorder.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class OrderRequest {

    @NotBlank(message = "customerId is required")
    private String customerId;

    @NotBlank(message = "restaurantId is required")
    private String restaurantId;

    @NotEmpty(message = "items list cannot be empty")
    private List<String> items;

    @Positive(message = "totalAmount must be greater than 0")
    private double totalAmount;

    @NotBlank(message = "deliveryAddress is required")
    private String deliveryAddress;

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
}
