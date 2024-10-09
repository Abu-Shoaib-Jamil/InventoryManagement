package com.nextuple.backend.controller;

import com.nextuple.backend.dto.OrderWrapper;
import com.nextuple.backend.entity.Inventory;
import com.nextuple.backend.entity.Order;
import com.nextuple.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(){
        List<Order> orders = orderService.getOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId){
        Order order = orderService.getOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.FOUND);
    }

    @PostMapping("/purchase")
    public ResponseEntity<HttpStatus> handlePurchaseOrder(@RequestBody List<OrderWrapper> orders){
        orderService.placePurchaseOrder(orders);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/sell")
    public ResponseEntity<HttpStatus> handleSellOrder(@RequestBody List<OrderWrapper> orders){
        orderService.placeSellOrder(orders);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
