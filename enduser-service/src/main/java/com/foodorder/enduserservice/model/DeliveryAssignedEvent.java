package com.foodorder.enduserservice.model;

import java.io.Serializable;
import java.time.Instant;

public class DeliveryAssignedEvent implements Serializable {
    private String orderId;
    private String deliveryPartnerId;
    private String deliveryAddress;
    private Instant assignedAt;

    public DeliveryAssignedEvent() {}

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getDeliveryPartnerId() { return deliveryPartnerId; }
    public void setDeliveryPartnerId(String deliveryPartnerId) { this.deliveryPartnerId = deliveryPartnerId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public Instant getAssignedAt() { return assignedAt; }
    public void setAssignedAt(Instant assignedAt) { this.assignedAt = assignedAt; }
}
