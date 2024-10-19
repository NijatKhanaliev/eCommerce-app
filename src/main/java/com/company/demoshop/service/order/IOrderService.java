package com.company.demoshop.service.order;

import com.company.demoshop.dto.OrderDto;
import com.company.demoshop.model.Order;

import java.util.List;


public interface IOrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrder(Long id);
    List<OrderDto> getUserOrders(Long userId);

}
