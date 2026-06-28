package com.pkenterprise.chemflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "invoices")
public class Invoice extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber; // e.g., "PKE/05/26-27"
    private LocalDate invoiceDate;

    // Buyer Details
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Financial Totals
    private BigDecimal subTotal;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal igstAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal roundedOff;
    private BigDecimal grandTotal;

    // One Invoice contains Many InvoiceItems
    // CascadeType.ALL means if we save an Invoice, it automatically saves all its items too
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();

    // Helper method to link items safely
    public void addItem(InvoiceItem invoiceItem){
        items.add(invoiceItem);
        invoiceItem.setInvoice(this);
    }
}
