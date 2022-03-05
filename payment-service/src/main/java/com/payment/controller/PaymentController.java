package com.payment.controller;


import com.core.auditing.Auditable;
import com.core.model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("payment/v1")
public class PaymentController {

    @Auditable
    @PostMapping
    public ResponseEntity<Payment> makePayment(@RequestBody Payment payment) {
        payment.setPaymentId(UUID.randomUUID().toString());

        return new ResponseEntity<>(payment, HttpStatus.ACCEPTED);
    }

}
