package com.foodorder.orderservice.kafka;

import com.foodorder.orderservice.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

    @Value("${app.topics.order-placed:order-placed}")
    private String orderPlacedTopic;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publishes the order keyed by orderId so all events for the same order
     * land on the same partition and preserve ordering downstream.
     */
    public void publishOrderPlaced(OrderEvent event) {
        kafkaTemplate.send(orderPlacedTopic, event.getOrderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish order {} to {}: {}",
                                event.getOrderId(), orderPlacedTopic, ex.getMessage(), ex);
                    } else {
                        log.info("Published order {} to partition {} offset {}",
                                event.getOrderId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
