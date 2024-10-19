package com.company.demoshop.service.cart;

import com.company.demoshop.dto.CartDto;
import com.company.demoshop.dto.CartItemDto;
import com.company.demoshop.exceptions.ResourceNotFoundException;
import com.company.demoshop.model.Cart;
import com.company.demoshop.model.User;
import com.company.demoshop.repository.CartItemRepository;
import com.company.demoshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);

        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);

        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);

        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalAmount(Long id) {
        Cart cart = getCart(id);

        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart cart = new Cart();
                    cart.setUser(user);

                   return cartRepository.save(cart);
                });
    }

    @Override
    public CartDto convetToCartDto(Cart cart){
        CartDto cartDto = modelMapper.map(cart,CartDto.class);

        Set<CartItemDto> allCartItemDto = cart.getCartItems()
                .stream()
                .map(item->modelMapper.map(item,CartItemDto.class))
                .collect(Collectors.toSet());

        cartDto.setCartItems(allCartItemDto);
        return cartDto;
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

}
