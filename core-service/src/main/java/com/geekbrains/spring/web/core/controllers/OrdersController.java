package com.geekbrains.spring.web.core.controllers;

import com.geekbrains.spring.web.api.exceptions.ResourceNotFoundException;
import com.geekbrains.spring.web.core.converters.OrderConverter;
import com.geekbrains.spring.web.api.core.OrderDetailsDto;
import com.geekbrains.spring.web.api.core.OrderDto;
import com.geekbrains.spring.web.core.services.OrderService;
import com.paypal.orders.ApplicationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    private final OrderConverter orderConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestHeader String username, @RequestBody OrderDetailsDto orderDetailsDto) {
        orderService.createOrder(username, orderDetailsDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String createdOrder(@RequestHeader Long id) {
        String status = "CREATED";
        orderService.addStatusOrder(id, status);
        return "CREATED";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String canceledOrder(@RequestHeader Long id) {
        orderService.removeOrderById(id);
        return "CANCELLED";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String paidedOrder(@RequestHeader Long id, ApplicationContext orderRequest) {
        if(orderRequest instanceof Array) {
            String status = "PAIDED";
            orderService.addStatusOrder(id, status);
        }
        return "PAIDED";
    }

    @GetMapping
    public List<OrderDto> getCurrentUserOrders(@RequestHeader String username) {
        return orderService.findOrdersByUsername(username).stream()
                .map(orderConverter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderConverter.entityToDto(orderService.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ORDER 404")));
    }
}
