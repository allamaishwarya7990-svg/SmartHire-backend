package com.example.smarthire.Controller;

import com.example.smarthire.Dto.JobSeekerProfileRequest;
import com.example.smarthire.Dto.JobSeekerProfileResponse;
import com.example.smarthire.Service.JobSeekerProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobseeker/profile")
public class JobSeekerProfileController {

    @Autowired
    private JobSeekerProfileService profileService;

    @GetMapping
    public ResponseEntity<JobSeekerProfileResponse> getProfile(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                profileService.getProfile(email)
        );
    }

    @PostMapping
    public ResponseEntity<JobSeekerProfileResponse> saveProfile(
            @RequestBody JobSeekerProfileRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                profileService.saveOrUpdateProfile(
                        email,
                        request
                )
        );
    }
}