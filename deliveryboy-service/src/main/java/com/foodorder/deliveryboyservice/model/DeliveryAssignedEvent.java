package com.foodorder.deliveryboyservice.model;

import java.io.Serializable;
import java.time.Instant;

/** Published to "order-out-for-delivery" once a delivery partner is assigned. */
public class DeliveryAssignedEvent implements Serializable {
    private String orderId;
    private String deliveryPartnerId;
    private String deliveryAddress;
    private Instant assignedAt;

    public DeliveryAssignedEvent() {}

    public DeliveryAssignedEvent(String orderId, String deliveryPartnerId, String deliveryAddress, Instant assignedAt) {
        this.orderId = orderId;
        this.deliveryPartnerId = deliveryPartnerId;
        this.deliveryAddress = deliveryAddress;
        this.assignedAt = assignedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getDeliveryPartnerId() { return deliveryPartnerId; }
    public void setDeliveryPartnerId(String deliveryPartnerId) { this.deliveryPartnerId = deliveryPartnerId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public Instant getAssignedAt() { return assignedAt; }
    public void setAssignedAt(Instant assignedAt) { this.assignedAt = assignedAt; }
}
