package com.pkenterprise.chemflow.service;

import com.pkenterprise.chemflow.model.Invoice;
import com.pkenterprise.chemflow.model.InvoiceItem;
import com.pkenterprise.chemflow.repository.CustomerRepository;
import com.pkenterprise.chemflow.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    public Invoice generateInvoice(Invoice invoice){
    
        // 0. Fetch the actual customer from the DB so the State Code is loaded for tax math!
        if (invoice.getCustomer() != null && invoice.getCustomer().getId() != null) {
            com.pkenterprise.chemflow.model.Customer dbCustomer = customerRepository.findById(invoice.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            invoice.setCustomer(dbCustomer);
        }

        // 1. Auto-Generate Invoice Date & Number
        LocalDate date = invoice.getInvoiceDate() == null ? LocalDate.now() : invoice.getInvoiceDate();
        invoice.setInvoiceDate(date);
        
        // Calculate Financial Year (April 1 to March 31)
        int year = date.getYear();
        int month = date.getMonthValue();
        LocalDate startOfFy;
        LocalDate endOfFy;
        String fyString;
        
        if (month >= 4) {
            startOfFy = LocalDate.of(year, 4, 1);
            endOfFy = LocalDate.of(year + 1, 3, 31);
            fyString = String.format("%02d-%02d", year % 100, (year + 1) % 100);
        } else {
            startOfFy = LocalDate.of(year - 1, 4, 1);
            endOfFy = LocalDate.of(year, 3, 31);
            fyString = String.format("%02d-%02d", (year - 1) % 100, year % 100);
        }

        long count = invoiceRepository.countByInvoiceDateBetween(startOfFy, endOfFy);
        String orderNumber = String.format("%02d", count + 1);
        
        invoice.setInvoiceNumber("PKE/" + orderNumber + "/" + fyString);

        BigDecimal subTotal = BigDecimal.ZERO;

        // 2. Loop through all products to calculate (Quantity * Rate)
        for (InvoiceItem item : invoice.getItems()){
            item.setInvoice(invoice); // Connect the child item to the parent invoice

            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal amount = item.getRate().multiply(quantity); // Quantity * Rate
            item.setAmount(amount);

            subTotal = subTotal.add(amount); // Keep a running total
        }

        invoice.setSubTotal(subTotal);

        // 3. The Tax Rules (GST)
        BigDecimal cgst = BigDecimal.ZERO;
        BigDecimal sgst = BigDecimal.ZERO;
        BigDecimal igst = BigDecimal.ZERO;
        // West Bengal State Code is "19"
        if (invoice.getCustomer() != null && "19".equals(invoice.getCustomer().getStateCode())) {
            // Intra-State: 9% CGST + 9% SGST
            cgst = subTotal.multiply(new BigDecimal("0.09"));
            sgst = subTotal.multiply(new BigDecimal("0.09"));
        } else {
            // Inter-State: 18% IGST
            igst = subTotal.multiply(new BigDecimal("0.18"));
        }
        invoice.setCgstAmount(cgst);
        invoice.setSgstAmount(sgst);
        invoice.setIgstAmount(igst);
        invoice.setTotalTaxAmount(cgst.add(sgst).add(igst));

        // 4. Calculate Grand Total and Rounding Off
        BigDecimal rawTotal = subTotal.add(cgst).add(sgst).add(igst);

        // Round to nearest whole Rupee (e.g., 49618.50 becomes 49619.00)
        BigDecimal grandTotal = rawTotal.setScale(0, RoundingMode.HALF_UP);
        BigDecimal roundedOff = grandTotal.subtract(rawTotal); // e.g., (+ 0.50)
        invoice.setGrandTotal(grandTotal);
        invoice.setRoundedOff(roundedOff);

        // 5. Save the Parent (and all its children) to the PostgreSQL database!
        return invoiceRepository.save(invoice);
    }
}
