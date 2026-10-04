package com.ecom.app.cart.service;

import com.ecom.app.cart.dto.OrderItemDTO;
import com.ecom.app.cart.dto.OrderResponse;
import com.ecom.app.cart.entity.CartItem;
import com.ecom.app.cart.entity.Order;
import com.ecom.app.cart.entity.OrderItem;
import com.ecom.app.cart.entity.OrderStatus;
import com.ecom.app.cart.repository.OrderRepository;
import com.ecom.app.user.entity.User;
import com.ecom.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService
{
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId)
    {
        //Validate cart items

        List<CartItem> cartItems = cartService.userCart(userId);
        if(cartItems.isEmpty())
        {
            return Optional.empty();
        }

        //Validate for user
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if (userOpt.isEmpty())
        {
            return Optional.empty();
        }
        User user = userOpt.get();

        //Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Create Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                        null,
                        cartItem.getProduct(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order

                ) )
                .toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        //clear the cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder)
    {
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getOrderStatus(),

                savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(
                                        new BigDecimal(orderItem.getQuantity())
                                )
                        ))
                        .toList(),

                savedOrder.getCreatedAt()

        );
    }
}
