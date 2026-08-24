package com.foodorder.deliveryboyservice.kafka;

import com.foodorder.deliveryboyservice.model.DeliveryAssignedEvent;
import com.foodorder.deliveryboyservice.model.OrderReadyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DeliveryAssigner {

    private static final Logger log = LoggerFactory.getLogger(DeliveryAssigner.class);

    // Stand-in for a real partner-matching service / DB lookup
    private static final List<String> AVAILABLE_PARTNERS = List.of(
            "partner-101", "partner-102", "partner-103", "partner-104");

    @Value("${app.topics.order-out-for-delivery:order-out-for-delivery}")
    private String outForDeliveryTopic;

    private final KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate;

    public DeliveryAssigner(KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "${app.topics.order-ready:order-ready}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderReady(OrderReadyEvent event, org.apache.kafka.clients.consumer.ConsumerRecord<String, OrderReadyEvent> record) {
        long kafkaLatencyMs = System.currentTimeMillis() - record.timestamp();
        log.info("KAFKA_LATENCY orderId={} latencyMs={}", event.getOrderId(), kafkaLatencyMs);
        log.info("Assigning delivery partner for order {}", event.getOrderId());

        String partnerId = AVAILABLE_PARTNERS.get(
                ThreadLocalRandom.current().nextInt(AVAILABLE_PARTNERS.size()));

        DeliveryAssignedEvent assigned = new DeliveryAssignedEvent(
                event.getOrderId(), partnerId, event.getDeliveryAddress(), Instant.now());

        kafkaTemplate.send(outForDeliveryTopic, assigned.getOrderId(), assigned);
        log.info("Order {} assigned to {} and published to {}",
                event.getOrderId(), partnerId, outForDeliveryTopic);
    }
}
