package io.github.yhugorocha.orders.service.impl;

import io.github.yhugorocha.orders.client.BankingServiceClient;
import io.github.yhugorocha.orders.client.ClientsClient;
import io.github.yhugorocha.orders.client.ProductClient;
import io.github.yhugorocha.orders.client.representation.ClientRepresentation;
import io.github.yhugorocha.orders.dto.*;
import io.github.yhugorocha.orders.exception.NotProcessException;
import io.github.yhugorocha.orders.exception.ResourceNotFoundException;
import io.github.yhugorocha.orders.model.OrderEntity;
import io.github.yhugorocha.orders.model.OrderItemEntity;
import io.github.yhugorocha.orders.model.enums.OrderStatus;
import io.github.yhugorocha.orders.model.enums.PaymentType;
import io.github.yhugorocha.orders.publisher.representantion.OrderItemRepresentation;
import io.github.yhugorocha.orders.publisher.representantion.OrderRepresentation;
import io.github.yhugorocha.orders.repository.OrderRepository;
import io.github.yhugorocha.orders.service.OrderService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import io.github.yhugorocha.orders.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value("${icompras.order.api.key}")
    private String apiKey;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final BankingServiceClient bankingServiceClient;
    private final ProductClient productClient;
    private final ClientsClient clientsClient;

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

    @Transactional
    public OrderResponseDto addNewPayment(Long id, PaymentDetailsRequestDto paymentDetails) {
        var order = this.findDetailedById(id);
        order.setDetails(paymentDetails.getDetails());
        order.setPaymentType(PaymentType.valueOf(paymentDetails.getPaymentType()));
        order.setStatus(OrderStatus.CREATED);
        order.setObservations(null);
        this.submitPaymentRequest(order);
        orderRepository.save(order);
        return OrderResponseDto.fromEntity(order);
    }

    private void submitPaymentRequest(OrderEntity createdOrder) {
        var paymentKey = bankingServiceClient.RequestPayment(createdOrder);
        createdOrder.setPaymentKey(paymentKey);
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

    @Transactional
    @Override
    public void processPaymentCallback(CallBackPaymentDto callback, String apiKey) {
        if (!Objects.equals(this.apiKey, apiKey)) {
            log.info("Chave de API inválida para callback de pagamento: {}", apiKey);
            throw new NotProcessException("Não foi possível processar o pagamento");
        }

        var order = orderRepository.findByIdAndPaymentKey(callback.getId(), callback.getPaymentKey())
                .orElseThrow(() -> new NotProcessException(
                        "Pedido não encontrado para a chave de pagamento: " + callback.getPaymentKey()
                ));

        if (callback.isStatus()) {
            order.setStatus(OrderStatus.PAID);
            order.setObservations(null);
            return;
        }

        order.setStatus(OrderStatus.PAYMENT_ERROR);
        order.setObservations(callback.getObservation());
    }

    public OrderRepresentation fetchCompleteOrderData(OrderEntity orderEntity){
        var listOrderItems = this.findOrderItems(orderEntity);
        var clientResponse = this.findClient(orderEntity.getClientId());

        return OrderRepresentation.toEntity(orderEntity, clientResponse, listOrderItems);
    }

    public List<OrderItemRepresentation> findOrderItems(OrderEntity orderEntity){
        var productIds = orderEntity.getItems().stream()
                .map(OrderItemEntity::getProductId)
                .toList();

        var response = productClient.findAllById(productIds);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new NotProcessException("Não foi possível validar os produtos do pedido");
        }

        return response.getBody().stream().map(product -> {
            var item = orderEntity.getItems().stream()
                    .filter(i -> i.getProductId().equals(product.id()))
                    .findFirst()
                    .orElseThrow(() -> new NotProcessException("Produto do pedido não encontrado: " + product.id()));

            return new OrderItemRepresentation(
                    item.getId(),
                    product.name(),
                    item.getQuantity(),
                    item.getUnitPrice()
            );
        }).toList();
    }

    public ClientRepresentation findClient(Long id){
        var response = clientsClient.findById(id);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new NotProcessException("Não foi possível validar os produtos do pedido");
        }
        return response.getBody();
    }
}
