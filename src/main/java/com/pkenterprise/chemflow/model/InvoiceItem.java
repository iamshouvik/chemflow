package com.pkenterprise.chemflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String particulars; // e.g., "N.C. Super Thinner"
    private String hsnCode;
    private Double quantity; // e.g., 320.0
    private BigDecimal rate;    // e.g., 115.00
    private BigDecimal amount;  // e.g., 36800.00

    // This links the item back to its parent Invoice
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonIgnore // Prevents infinite loops when returning JSON to the frontend
    private Invoice invoice;
}
