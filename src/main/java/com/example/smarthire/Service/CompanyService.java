package com.example.smarthire.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.smarthire.Dto.CompanyDto;
import com.example.smarthire.Entity.Company;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.CompanyRepository;
import com.example.smarthire.Repository.UserRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public CompanyDto getCompanyByRecruiterEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findByUser(user)
                .orElseGet(() -> {
                    Company newComp = new Company();
                    newComp.setUser(user);
                    newComp.setCompanyName(user.getFullName() != null ? user.getFullName() + " Company" : "My Company");
                    return companyRepository.save(newComp);
                });

        return convertToDto(company);
    }

    public CompanyDto updateCompanyProfile(String email, CompanyDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"RECRUITER".equals(user.getRole())) {
            throw new RuntimeException("Only recruiters can manage company profiles");
        }

        Company company = companyRepository.findByUser(user)
                .orElseGet(() -> {
                    Company newComp = new Company();
                    newComp.setUser(user);
                    return newComp;
                });

        if (dto.getCompanyName() != null) company.setCompanyName(dto.getCompanyName());
        if (dto.getIndustry() != null) company.setIndustry(dto.getIndustry());
        if (dto.getCompanySize() != null) company.setCompanySize(dto.getCompanySize());
        if (dto.getWebsite() != null) company.setWebsite(dto.getWebsite());
        if (dto.getLocation() != null) company.setLocation(dto.getLocation());
        if (dto.getEmail() != null) company.setEmail(dto.getEmail());
        if (dto.getPhone() != null) company.setPhone(dto.getPhone());
        if (dto.getFounded() != null) company.setFounded(dto.getFounded());
        if (dto.getDescription() != null) company.setDescription(dto.getDescription());

        Company saved = companyRepository.save(company);
        return convertToDto(saved);
    }

    public CompanyDto convertToDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        dto.setIndustry(company.getIndustry());
        dto.setCompanySize(company.getCompanySize());
        dto.setWebsite(company.getWebsite());
        dto.setLocation(company.getLocation());
        dto.setEmail(company.getEmail());
        dto.setPhone(company.getPhone());
        dto.setFounded(company.getFounded());
        dto.setDescription(company.getDescription());
        dto.setIsVerified(company.getIsVerified());
        return dto;
    }
}
