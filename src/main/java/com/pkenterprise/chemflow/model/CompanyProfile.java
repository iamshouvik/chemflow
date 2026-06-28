package com.pkenterprise.chemflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company_profiles")
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String subtitle; // e.g., "General Order Suppliers"
    private String address;
    private String phone;
    private String gstin;

    // Bank Details
    private String bankName;
    private String branch;
    private String ifsc;
    private String accountNumber;

}
