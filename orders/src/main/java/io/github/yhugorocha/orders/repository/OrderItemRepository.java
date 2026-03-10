package io.github.yhugorocha.orders.repository;

import io.github.yhugorocha.orders.model.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
