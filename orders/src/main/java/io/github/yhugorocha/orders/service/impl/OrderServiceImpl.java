package io.github.yhugorocha.orders.service.impl;

import io.github.yhugorocha.orders.client.BankingServiceClient;
import io.github.yhugorocha.orders.dto.OrderCreateRequestDto;
import io.github.yhugorocha.orders.dto.OrderItemRequestDto;
import io.github.yhugorocha.orders.dto.OrderResponseDto;
import io.github.yhugorocha.orders.dto.OrderStatusUpdateRequestDto;
import io.github.yhugorocha.orders.exception.ResourceNotFoundException;
import io.github.yhugorocha.orders.model.OrderEntity;
import io.github.yhugorocha.orders.model.OrderItemEntity;
import io.github.yhugorocha.orders.model.enums.OrderStatus;
import io.github.yhugorocha.orders.repository.OrderRepository;
import io.github.yhugorocha.orders.service.OrderService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import io.github.yhugorocha.orders.validator.OrderValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final BankingServiceClient bankingServiceClient;

    @Override
    @Transactional
    public OrderResponseDto create(OrderCreateRequestDto request) {
        var order = request.toEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        var items = OrderItemRequestDto.toEntityList(request.getItems());
        items.forEach(order::addItem);
        order.setTotal(this.calculateTotal(items));

        orderValidator.validateOrder(order);
        var createdOrder = orderRepository.save(order);
        this.submitPaymentRequest(createdOrder);
        return OrderResponseDto.fromEntity(createdOrder);
    }

    private void submitPaymentRequest(OrderEntity createdOrder) {
        var paymentKey = bankingServiceClient.RequestPayment(createdOrder);
        createdOrder.setPaymentKey(paymentKey);
    }

    @Override
    @Transactional
    public OrderResponseDto updateStatus(Long orderId, OrderStatusUpdateRequestDto request) {
        var order = this.findDetailedById(orderId);
        order.setStatus(request.getStatus());

        var updatedOrder = orderRepository.save(order);
        return OrderResponseDto.fromEntity(updatedOrder);
    }

    private OrderEntity findDetailedById(Long orderId) {
        return orderRepository.findDetailedById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado para o id: " + orderId));
    }

    private BigDecimal calculateTotal(List<OrderItemEntity> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
