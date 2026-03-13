package io.github.yhugorocha.orders.model;

import io.github.yhugorocha.orders.model.enums.OrderStatus;
import io.github.yhugorocha.orders.model.enums.PaymentType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "observations")
    private String observations;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "total", nullable = false, precision = 16, scale = 2)
    private BigDecimal total;

    @Column(name = "tracking_code")
    private String trackingCode;

    @Column(name = "invoice_url")
    private String invoiceUrl;

    @Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Transient
    private String details;

    @Transient
    private PaymentType paymentType;

    public void addItem(OrderItemEntity item) {
        this.items.add(item);
        item.setOrder(this);
    }
}
