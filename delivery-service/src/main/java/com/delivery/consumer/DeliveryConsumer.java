package com.delivery.consumer;

import com.core.auditing.Auditable;
import com.core.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryConsumer.class);
    @Auditable
    @KafkaListener(topics = "delivery", groupId = "delivery")
    public void deliverOrder(Order order) {

        LOGGER.info("Delivering order {}",order.getOrderId());

    }
}
