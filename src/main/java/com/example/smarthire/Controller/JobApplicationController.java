package com.example.smarthire.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.smarthire.Dto.JobApplicationDto;
import com.example.smarthire.Service.JobApplicationService;

@RestController
public class JobApplicationController {

    @Autowired
    private JobApplicationService applicationService;

    // Job Seeker Application Endpoints
    @PostMapping("/api/jobseeker/applications")
    public ResponseEntity<JobApplicationDto> applyForJob(
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {
        String email = authentication.getName();
        Long jobId = Long.valueOf(payload.get("jobId").toString());
        String coverMessage = (String) payload.get("coverMessage");
        String resumePath = (String) payload.get("resumePath");
        return ResponseEntity.ok(applicationService.applyForJob(email, jobId, coverMessage, resumePath));
    }

    @GetMapping("/api/jobseeker/applications")
    public ResponseEntity<List<JobApplicationDto>> getSeekerApplications(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getSeekerApplications(email));
    }

    @GetMapping("/api/jobseeker/applications/{id}")
    public ResponseEntity<JobApplicationDto> getApplicationDetails(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplicationById(id, email));
    }

    // Recruiter Application Endpoints
    @GetMapping("/api/recruiter/applications")
    public ResponseEntity<List<JobApplicationDto>> getRecruiterApplications(
            @RequestParam(required = false) Long jobId,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getRecruiterApplications(email, jobId));
    }

    @GetMapping("/api/recruiter/applications/{id}")
    public ResponseEntity<JobApplicationDto> getRecruiterApplicationDetails(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplicationById(id, email));
    }

    @PutMapping("/api/recruiter/applications/{id}/status")
    public ResponseEntity<JobApplicationDto> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload,
            Authentication authentication) {
        String email = authentication.getName();
        String status = payload.get("status");
        return ResponseEntity.ok(applicationService.updateApplicationStatus(email, id, status));
    }
}
