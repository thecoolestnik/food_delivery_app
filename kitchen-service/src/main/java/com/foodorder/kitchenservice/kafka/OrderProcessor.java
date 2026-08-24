package com.foodorder.kitchenservice.kafka;

import com.foodorder.kitchenservice.model.OrderEvent;
import com.foodorder.kitchenservice.model.OrderReadyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderProcessor {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessor.class);

    @Value("${app.topics.order-ready:order-ready}")
    private String orderReadyTopic;

    private final KafkaTemplate<String, OrderReadyEvent> kafkaTemplate;

    public OrderProcessor(KafkaTemplate<String, OrderReadyEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "${app.topics.order-placed:order-placed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderPlaced(OrderEvent event, org.apache.kafka.clients.consumer.ConsumerRecord<String, OrderEvent> record) {
        long kafkaLatencyMs = System.currentTimeMillis() - record.timestamp();
        log.info("KAFKA_LATENCY orderId={} latencyMs={}", event.getOrderId(), kafkaLatencyMs);
        log.info("Kitchen received order {} for restaurant {}", event.getOrderId(), event.getRestaurantId());

        if (event.getItems() == null || event.getItems().isEmpty()) {
            // Non-retryable: this will never succeed, so it goes straight to the DLQ
            throw new IllegalArgumentException("Order " + event.getOrderId() + " has no items");
        }

        // Simulated prep work. Any transient exception here is retried by the
        // error handler before falling back to the DLQ.
        simulatePreparation(event);

        OrderReadyEvent readyEvent = new OrderReadyEvent(
                event.getOrderId(), event.getRestaurantId(), event.getDeliveryAddress(), Instant.now());

        kafkaTemplate.send(orderReadyTopic, readyEvent.getOrderId(), readyEvent);
        log.info("Order {} marked ready, published to {}", event.getOrderId(), orderReadyTopic);
    }

    private void simulatePreparation(OrderEvent event) {
        try {
            Thread.sleep(50); // stand-in for real prep-time / inventory-check work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
