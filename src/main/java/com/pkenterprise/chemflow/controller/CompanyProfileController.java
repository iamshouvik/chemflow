package com.pkenterprise.chemflow.controller;

import com.pkenterprise.chemflow.model.CompanyProfile;
import com.pkenterprise.chemflow.service.CompanyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyProfileController {

    private final CompanyProfileService companyProfileService;

    @GetMapping
    public CompanyProfile getCompanyProfile() {
        return companyProfileService.getCompanyProfile();
    }

    @PutMapping
    public CompanyProfile updateCompanyProfile(@RequestBody CompanyProfile profileDetails) {
        return companyProfileService.updateCompanyProfile(profileDetails);
    }
}
