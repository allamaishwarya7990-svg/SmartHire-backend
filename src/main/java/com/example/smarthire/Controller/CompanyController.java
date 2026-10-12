package com.example.smarthire.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.smarthire.Dto.CompanyDto;
import com.example.smarthire.Service.CompanyService;

@RestController
@RequestMapping("/api/recruiter/company")
@CrossOrigin(origins = "http://localhost:5173")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<CompanyDto> getCompanyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(companyService.getCompanyByRecruiterEmail(email));
    }

    @PutMapping
    public ResponseEntity<CompanyDto> updateCompanyProfile(
            @RequestBody CompanyDto dto,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(companyService.updateCompanyProfile(email, dto));
    }
}
