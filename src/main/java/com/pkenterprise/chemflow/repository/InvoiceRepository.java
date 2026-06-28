package com.pkenterprise.chemflow.repository;

import com.pkenterprise.chemflow.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    long countByInvoiceDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
