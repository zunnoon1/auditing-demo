package com.order.service;

import com.core.model.Order;
import com.core.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger("OrderService");
    @Value("${payment.service.url}")
    String paymentUrl;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    RestTemplate restTemplate;

    public Order createOrder(Order order) throws Exception{

        ResponseEntity<Payment> paymentResponse = makePayment(order);

        if (paymentResponse.getStatusCode().is2xxSuccessful()) {
            deliverOrder(order);
            return order;
        } else {
            return null;
        }
    }

    private void deliverOrder(Order order) {
        kafkaTemplate.send("delivery",order).addCallback(s->{
            LOGGER.info("Order is successfully sent for deliver");
        },f ->{
            LOGGER.error("Order could not be sent {}",f);

        });
    }

    private ResponseEntity<Payment> makePayment(Order order) throws Exception {

        Payment payment = new Payment();
        payment.setAmount(order.getItems().stream().mapToDouble(x -> x.getQuantity() * x.getUnitPrice()).sum());
        payment.setOrderId(order.getOrderId());
        payment.setCustomer(order.getCustomer());

        RequestEntity<Payment> paymentRequest = new RequestEntity(payment, HttpMethod.POST,new URI(paymentUrl));
        return restTemplate.exchange(paymentRequest,Payment.class);
    }
}
