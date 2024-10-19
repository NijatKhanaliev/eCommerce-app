package com.company.demoshop.service.cart;

import com.company.demoshop.dto.CartDto;
import com.company.demoshop.model.Cart;
import com.company.demoshop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalAmount(Long id);

    Cart initializeNewCart(User user);

    CartDto convetToCartDto(Cart cart);

    Cart getCartByUserId(Long userId);
}
