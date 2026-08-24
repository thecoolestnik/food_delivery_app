package com.foodorder.enduserservice.kafka;

import com.foodorder.enduserservice.model.DeliveryAssignedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @KafkaListener(
            topics = "${app.topics.order-out-for-delivery:order-out-for-delivery}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleDeliveryAssigned(DeliveryAssignedEvent event) {
        // In production this would call spring-boot-starter-mail / an FCM push
        // provider. Kept as a logged notification here so the whole pipeline
        // runs end-to-end locally without needing real SMTP/push credentials.
        log.info("NOTIFY customer: your order {} is on its way with partner {} to {}",
                event.getOrderId(), event.getDeliveryPartnerId(), event.getDeliveryAddress());
    }
}
