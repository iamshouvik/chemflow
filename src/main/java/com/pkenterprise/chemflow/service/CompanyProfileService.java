package com.pkenterprise.chemflow.service;

import com.pkenterprise.chemflow.model.CompanyProfile;
import com.pkenterprise.chemflow.repository.CompanyProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;

    public CompanyProfile getCompanyProfile() {
        // Assuming ID 1 is the main company profile
        return companyProfileRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Company Profile not found with id 1"));
    }

    public CompanyProfile updateCompanyProfile(CompanyProfile profileDetails) {
        CompanyProfile profile = getCompanyProfile();
        profile.setCompanyName(profileDetails.getCompanyName());
        profile.setSubtitle(profileDetails.getSubtitle());
        profile.setAddress(profileDetails.getAddress());
        profile.setPhone(profileDetails.getPhone());
        profile.setGstin(profileDetails.getGstin());
        profile.setBankName(profileDetails.getBankName());
        profile.setAccountNumber(profileDetails.getAccountNumber());
        profile.setBranch(profileDetails.getBranch());
        profile.setIfsc(profileDetails.getIfsc());
        return companyProfileRepository.save(profile);
    }
}
