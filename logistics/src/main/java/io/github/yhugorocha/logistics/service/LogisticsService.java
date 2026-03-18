package io.github.yhugorocha.logistics.service;

import io.github.yhugorocha.logistics.publisher.LogisticsPublisher;
import io.github.yhugorocha.logistics.publisher.representation.StatusUpdateLogistics;
import io.github.yhugorocha.logistics.subscriber.representation.OrderStatus;
import io.github.yhugorocha.logistics.subscriber.representation.StatusUpdateInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class LogisticsService {

    private final LogisticsPublisher logisticsPublisher;

    public void generateTrackingNumber(StatusUpdateInvoice updateInvoice) {
        var trackingNumber = UUID.randomUUID().toString();
        logisticsPublisher.publisher(new StatusUpdateLogistics(updateInvoice.id(), OrderStatus.SHIPPED, trackingNumber));
    }
}
