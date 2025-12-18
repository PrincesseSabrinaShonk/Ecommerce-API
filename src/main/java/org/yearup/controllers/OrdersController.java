package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.models.Order;

// This class handles HTTP requests and returns data
@RestController
// This sets the base URL for all endpoints in the controller.
@RequestMapping(value = "/orders", produces = "application/json")
    public class OrdersController {

     //Temporary Order object.
     // In a full implementation, this would be created from the user's shopping cart
     // and persisted to the database.

        Order order = new Order();

        @PostMapping
        public ResponseEntity<Order> createOrder() { // create order from cart
            return ResponseEntity.status(HttpStatus.CREATED).body(order); // Return the order with HTTP 201 Created status
        }
    }

