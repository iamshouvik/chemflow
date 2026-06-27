package com.pkenterprise.chemflow.repository;

import com.pkenterprise.chemflow.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    RawMaterial findByName(String name);
}
