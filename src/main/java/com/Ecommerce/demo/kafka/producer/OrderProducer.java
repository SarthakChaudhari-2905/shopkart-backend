package com.Ecommerce.demo.kafka.producer;

import com.Ecommerce.demo.kafka.dto.OrderEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderProducer(
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(
            OrderEvent event
    ) {

        kafkaTemplate.send(
                "order-topic",
                event
        );

        System.out.println(
                "Order Event Sent To Kafka : "
                        + event.getOrderId()
        );
    }
}
