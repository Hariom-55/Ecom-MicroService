package com.ecom.app.cart.controller;

import com.ecom.app.cart.dto.CartItemRequest;
import com.ecom.app.cart.entity.CartItem;
import com.ecom.app.cart.service.CartService;
import com.ecom.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController
{
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader ("X-User-ID") String userId,
            @RequestBody CartItemRequest request
    ){
        if(!cartService.addToCart(userId, request))
        {
            return ResponseEntity.badRequest()
                    .body(
                            "Product Out of Stock User Not Found or Product does not exist"
                    );
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
            @RequestHeader ("X-User-ID") String userId,
            @PathVariable Long productId
    ){
        boolean deleted = cartService.removeItemFromCart(userId, productId);

        return deleted ?  ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> userCart(
            @RequestHeader ("X-User-ID") String userId
    ){
        return ResponseEntity.ok(cartService.userCart(userId));
    }
}
