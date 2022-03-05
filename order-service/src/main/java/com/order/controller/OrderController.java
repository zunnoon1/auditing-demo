package com.order.controller;

import com.core.auditing.Auditable;
import com.core.model.Order;
import com.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("order/v1")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Auditable
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            order.setOrderId(UUID.randomUUID().toString());
            order = orderService.createOrder(order);

            return new ResponseEntity(order,HttpStatus.CREATED);

        } catch (Exception e) {

            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
