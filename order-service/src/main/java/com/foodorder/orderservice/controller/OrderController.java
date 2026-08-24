package com.foodorder.orderservice.controller;

import com.foodorder.orderservice.dto.OrderRequest;
import com.foodorder.orderservice.kafka.OrderProducer;
import com.foodorder.orderservice.model.OrderEvent;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> placeOrder(@Valid @RequestBody OrderRequest request) {
        String orderId = UUID.randomUUID().toString();

        OrderEvent event = new OrderEvent(
                orderId,
                request.getCustomerId(),
                request.getRestaurantId(),
                request.getItems(),
                request.getTotalAmount(),
                request.getDeliveryAddress(),
                Instant.now()
        );

        orderProducer.publishOrderPlaced(event);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("orderId", orderId, "status", "PLACED"));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("order-service is up");
    }
}
