package io.github.yhugorocha.invoicing.service;

import io.github.yhugorocha.invoicing.bucket.BucketFile;
import io.github.yhugorocha.invoicing.bucket.BucketService;
import io.github.yhugorocha.invoicing.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class InvoiceService {

    @Value("classpath:reports/Invoice.jrxml")
    private Resource invoice;
    
    @Value("classpath:reports/logo.png")
    private Resource logo;

    @Value("classpath:reports/pago.png")
    private Resource pago;

    private final BucketService bucketService;

    public void generateInvoice(Order order) {
        try {
            byte[] pdfBytes = this.buildInvoice(order);
            var fileName = String.format("notaFiscal_Order_%d.pdf", order.id());

            bucketService.upload(new BucketFile(fileName, new ByteArrayInputStream(pdfBytes),
                    MediaType.APPLICATION_PDF, pdfBytes.length));
        } catch (Exception e) {
            log.error("Error generating invoice for order id: {}", order.id(), e);
            throw new RuntimeException(e);
        }
    }

    public byte[] buildInvoice(Order order){
        log.info("Building invoice for order id: {}", order.id());

        try (InputStream inputStream = invoice.getInputStream()) {
            var params = this.getParams(order);
            var dataSource = new JRBeanCollectionDataSource(order.items());

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getParams(Order order) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("NAME", order.client().name());
        params.put("CPF", order.client().cpf());
        params.put("STREET", order.client().street());
        params.put("NUMBER", order.client().number());
        params.put("NEIGHBORHOOD", order.client().neighborhood());
        params.put("EMAIL", order.client().email());
        params.put("PHONE", order.client().phone());
        params.put("ID", order.id());
        params.put("ORDER_DATE", Timestamp.valueOf(order.orderDate()));
        params.put("TOTAL_ORDER", order.total());
        params.put("LOGO", logo.getFile().getAbsolutePath());
        params.put("PAGO", pago.getFile().getAbsolutePath());

        return params;
    }
}