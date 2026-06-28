package com.pkenterprise.chemflow.repository;

import com.pkenterprise.chemflow.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
