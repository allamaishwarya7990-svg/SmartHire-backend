package com.example.smarthire.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.smarthire.Dto.JobDto;
import com.example.smarthire.Service.JobService;

@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    // Public & JobSeeker Job Endpoints
    @GetMapping("/api/jobs/public")
    public ResponseEntity<List<JobDto>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String experience,
            Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(jobService.searchJobs(keyword, location, type, experience, email));
    }

    @GetMapping("/api/jobs/public/{id}")
    public ResponseEntity<JobDto> getJobDetails(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(jobService.getJobById(id, email));
    }

    // Recruiter Job Endpoints
    @GetMapping("/api/recruiter/jobs")
    public ResponseEntity<List<JobDto>> getRecruiterJobs(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(jobService.getRecruiterJobs(email));
    }

    @PostMapping("/api/recruiter/jobs")
    public ResponseEntity<JobDto> createJob(
            @RequestBody JobDto dto,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(jobService.createJob(email, dto));
    }

    @PutMapping("/api/recruiter/jobs/{id}")
    public ResponseEntity<JobDto> updateJob(
            @PathVariable Long id,
            @RequestBody JobDto dto,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(jobService.updateJob(email, id, dto));
    }

    @PutMapping("/api/recruiter/jobs/{id}/close")
    public ResponseEntity<JobDto> closeJob(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(jobService.closeJob(email, id));
    }

    @PutMapping("/api/recruiter/jobs/{id}/reopen")
    public ResponseEntity<JobDto> reopenJob(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(jobService.reopenJob(email, id));
    }

    @DeleteMapping("/api/recruiter/jobs/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        jobService.deleteJob(email, id);
        return ResponseEntity.noContent().build();
    }
}
