package com.pkenterprise.chemflow.service;

import com.pkenterprise.chemflow.model.RawMaterial;
import com.pkenterprise.chemflow.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterial addRawMaterial(RawMaterial rawMaterial){
        // TODO: Later, we can add logic here like: if (material.getName() == null) throw Error
        return rawMaterialRepository.save(rawMaterial);
    }

    public List<RawMaterial> getAllRawMaterials(){
        return rawMaterialRepository.findAll();
    }
}
