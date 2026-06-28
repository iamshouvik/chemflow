package com.pkenterprise.chemflow.controller;

import com.pkenterprise.chemflow.model.Invoice;
import com.pkenterprise.chemflow.repository.InvoiceRepository;
import com.pkenterprise.chemflow.service.InvoiceService;
import com.pkenterprise.chemflow.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;
    private final PdfGeneratorService pdfGeneratorService;

    // Endpoint: Generate and save an invoice
    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @PostMapping
    public Invoice createInvoice(@RequestBody Invoice invoice) {
        return invoiceService.generateInvoice(invoice);
    }

    // GET http://localhost:8080/api/invoices/1/pdf
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) throws Exception {

        // 1. Fetch the saved invoice from PostgreSQL
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found!"));

        // 2. Generate the PDF bytes
        byte[] pdfBytes = pdfGeneratorService.generateInvoicePdf(invoice);

        // 3. Tell the browser/Postman that this is a downloadable PDF file
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Invoice_" + invoice.getInvoiceNumber() + ".pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
