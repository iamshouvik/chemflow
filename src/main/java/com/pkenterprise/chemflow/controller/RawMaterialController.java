package com.pkenterprise.chemflow.controller;

import com.pkenterprise.chemflow.model.RawMaterial;
import com.pkenterprise.chemflow.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @PostMapping
    public RawMaterial addRawMaterial(@RequestBody RawMaterial rawMaterial){
        return rawMaterialService.addRawMaterial(rawMaterial);
    }

    @GetMapping
    public List<RawMaterial> getAllRawMaterials(){
        return rawMaterialService.getAllRawMaterials();
    }
}
