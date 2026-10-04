package com.ecom.app.cart.service;

import com.ecom.app.cart.dto.CartItemRequest;
import com.ecom.app.cart.entity.CartItem;
import com.ecom.app.cart.repository.CartItemRepository;
import com.ecom.app.product.entity.Product;
import com.ecom.app.product.repository.ProductRepository;
import com.ecom.app.user.entity.User;
import com.ecom.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class CartService
{
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userid, CartItemRequest request)
    {
        // Look for product
        Optional<Product> productOpt = productRepository.findById(
                request.getProductId()
        );

        if(productOpt.isEmpty())
            return false;

        Product product = productOpt.get();
        if(product.getStockQuantity() < request.getQuantity())
            return false;

        Optional<User> userOpt = userRepository.findById(
                Long.valueOf(userid)
        );

        if(userOpt.isEmpty())
            return false;

        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository
                .findByUserAndProduct(
                        user, product
                );

        if(existingCartItem != null)
        {
            //update the cart item(quantity)
            existingCartItem.setQuantity(
                    existingCartItem.getQuantity() + request.getQuantity()
            );

            existingCartItem.setPrice(
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            existingCartItem.getQuantity()
                                    ))
            );

            cartItemRepository.save(existingCartItem);
        }else{
            //Create new Cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());

            cartItem.setPrice(product.getPrice().multiply(
                    BigDecimal.valueOf(request.getQuantity())
            ));

            cartItemRepository.save(cartItem);
        }

        return true;

    }

    public boolean removeItemFromCart(String userId, Long productId)
    {
        Optional<Product> productOpt = productRepository.findById(
                productId
        );


        Optional<User> userOpt = userRepository.findById(
                Long.valueOf(userId)
        );


        if (productOpt.isPresent() && userOpt.isPresent())
        {
            cartItemRepository.deleteByUserAndProduct(
                    userOpt.get(),
                    productOpt.get()
            );

            return true;
        }

        return false;

    }

    public List<CartItem> userCart(String userId)
    {

        return userRepository.findById(
                Long.valueOf(userId)
                )
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);


    }

    public void clearCart(String userId)
    {
        userRepository.findById(Long.valueOf(userId)).ifPresent(
                cartItemRepository::deleteByUser
        );
    }
}
