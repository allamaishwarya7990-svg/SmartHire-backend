package com.example.smarthire.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.smarthire.Dto.SavedJobDto;
import com.example.smarthire.Service.SavedJobService;

@RestController
@RequestMapping("/api/jobseeker/saved-jobs")
public class SavedJobController {

    @Autowired
    private SavedJobService savedJobService;

    @GetMapping
    public ResponseEntity<List<SavedJobDto>> getSavedJobs(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(savedJobService.getSavedJobs(email));
    }

    @PostMapping
    public ResponseEntity<SavedJobDto> saveJob(
            @RequestBody Map<String, Long> payload,
            Authentication authentication) {
        String email = authentication.getName();
        Long jobId = payload.get("jobId");
        return ResponseEntity.ok(savedJobService.saveJob(email, jobId));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> unsaveJob(
            @PathVariable Long jobId,
            Authentication authentication) {
        String email = authentication.getName();
        savedJobService.unsaveJob(email, jobId);
        return ResponseEntity.noContent().build();
    }
}
