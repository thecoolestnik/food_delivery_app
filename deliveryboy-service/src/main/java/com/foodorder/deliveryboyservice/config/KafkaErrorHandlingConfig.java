package com.foodorder.deliveryboyservice.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Retry + Dead-Letter-Queue strategy shared by every consumer in this service.
 *
 * Flow: a failed message is retried 3 times with a 2s gap. If it still fails
 * (e.g. malformed payload, downstream bug), it is published to
 * "<original-topic>.DLT" instead of being silently dropped or blocking the
 * partition forever. This is what guarantees zero message loss under
 * simulated broker/consumer failures.
 */
@Configuration
public class KafkaErrorHandlingConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaErrorHandlingConfig.class);

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> {
                    log.error("Exhausted retries for record on topic {} partition {} offset {}: {}",
                            record.topic(), record.partition(), record.offset(), ex.getMessage());
                    return new org.apache.kafka.common.TopicPartition(record.topic() + ".DLT", record.partition());
                });

        // 3 retries, 2 seconds apart, before giving up and routing to the DLT
        FixedBackOff backOff = new FixedBackOff(2000L, 3L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        // Don't retry on deserialization/validation errors — they'll never succeed on retry
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        errorHandler.setRetryListeners((record, ex, deliveryAttempt) ->
                log.warn("Retry attempt {} for order event on partition {} offset {}: {}",
                        deliveryAttempt, record.partition(), record.offset(), ex.getMessage()));

        return errorHandler;
    }
}
