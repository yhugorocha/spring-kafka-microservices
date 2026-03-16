package io.github.yhugorocha.invoicing.service;

import io.github.yhugorocha.invoicing.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvoiceService {

    public void buildInvoice(Order order){
        log.info("Building invoice for order id: {}", order.id());
    }
}