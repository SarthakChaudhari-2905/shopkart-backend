package com.Ecommerce.demo.kafka.consumer;

import com.Ecommerce.demo.kafka.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(
            topics = "order-topic",
            groupId = "ecommerce-group"
    )
    public void consume(
            OrderEvent event
    ) {

        System.out.println(
                "Received Order Event : "
                        + event.getOrderId()
        );

        System.out.println(
                "Customer : "
                        + event.getCustomerEmail()
        );

        System.out.println(
                "Amount : "
                        + event.getTotalAmount()
        );
    }
}