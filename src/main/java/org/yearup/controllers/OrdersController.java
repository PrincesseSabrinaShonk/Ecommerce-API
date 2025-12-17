package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.models.Order;

@RestController
@RequestMapping(value = "/orders", produces = "application/json")
    public class OrdersController {

        Order order = new Order();
        @PostMapping
        public ResponseEntity<Order> createOrder() { // create order from cart
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        }
    }

