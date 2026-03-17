package io.github.yhugorocha.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem{
    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
}