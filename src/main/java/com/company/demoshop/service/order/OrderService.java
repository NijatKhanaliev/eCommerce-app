package com.company.demoshop.service.order;

import com.company.demoshop.dto.OrderDto;
import com.company.demoshop.dto.OrderItemDto;
import com.company.demoshop.enums.OrderStatus;
import com.company.demoshop.exceptions.ResourceNotFoundException;
import com.company.demoshop.model.Cart;
import com.company.demoshop.model.Order;
import com.company.demoshop.model.OrderItem;
import com.company.demoshop.model.Product;
import com.company.demoshop.repository.OrderRepository;
import com.company.demoshop.repository.ProductRepository;
import com.company.demoshop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public OrderDto placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order,cart);

        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return convertToDto(savedOrder);
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(cart.getUser());

        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
      return  cart.getCartItems().stream().map(item->{
            Product product = item.getProduct();
            product.setInventory(product.getInventory() - item.getQuantity());
            productRepository.save(product);

            return new OrderItem(order,product,item.getUnitPrice(),item.getQuantity());
        }).toList();
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems){
        return orderItems
                .stream()
                .map(item->item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream().map(this::convertToDto).toList();
    }


    private OrderDto convertToDto(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        List<OrderItemDto> orderAllItemDto = order.getOrderItems().stream().map(item->modelMapper.map(item, OrderItemDto.class)).toList();
        orderDto.setItems(orderAllItemDto);

        return orderDto;
    }

}
