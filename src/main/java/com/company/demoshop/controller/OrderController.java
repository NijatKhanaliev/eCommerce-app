package com.company.demoshop.controller;

import com.company.demoshop.dto.OrderDto;
import com.company.demoshop.exceptions.ResourceNotFoundException;
import com.company.demoshop.model.Order;
import com.company.demoshop.response.ApiResponse;
import com.company.demoshop.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try {
            OrderDto order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Success!",order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Create Order Failed!",e.getMessage()));
        }
    }

    @GetMapping("/{id}/order")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long id){
        try {
            OrderDto order = orderService.getOrder(id);
            return ResponseEntity.ok(new ApiResponse("Success!",order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        List<OrderDto> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(new ApiResponse("Success!",orders));
    }

}
