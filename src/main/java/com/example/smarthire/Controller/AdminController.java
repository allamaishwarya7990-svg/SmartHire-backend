package com.example.smarthire.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.smarthire.Dto.AdminDashboardStatsDto;
import com.example.smarthire.Dto.JobApplicationDto;
import com.example.smarthire.Dto.JobDto;
import com.example.smarthire.Dto.ReportDto;
import com.example.smarthire.Service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // =========================
    // DASHBOARD STATISTICS
    // =========================

    @GetMapping("/dashboard/stats")
    public ResponseEntity<AdminDashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // =========================
    // JOBSEEKERS
    // =========================

    @GetMapping("/jobseekers")
    public ResponseEntity<List<Map<String, Object>>> getJobSeekers() {
        return ResponseEntity.ok(adminService.getAllJobSeekers());
    }

    // =========================
    // SUSPEND / REACTIVATE USER
    // Works for jobseekers and recruiters
    // =========================

    @PutMapping("/users/{id}/status")
    public ResponseEntity<Map<String, String>> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "A valid user ID is required")
            );
        }

        if (payload == null || payload.get("status") == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Status is required")
            );
        }

        String status = payload.get("status").trim().toUpperCase();

        if (!status.equals("ACTIVE") && !status.equals("SUSPENDED")) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Status must be ACTIVE or SUSPENDED")
            );
        }

        try {
            adminService.updateUserStatus(id, status);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "User status updated successfully",
                            "status", status
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "message",
                            e.getMessage() != null
                                    ? e.getMessage()
                                    : "User not found"
                    )
            );
        }
    }

    // =========================
    // DELETE USER
    // =========================

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // RECRUITERS
    // =========================

    @GetMapping("/recruiters")
    public ResponseEntity<List<Map<String, Object>>> getRecruiters() {
        return ResponseEntity.ok(adminService.getAllRecruiters());
    }

    // =========================
    // RECRUITER COMPANY VERIFICATION
    // The ID must be the company ID
    // =========================

    @PutMapping("/recruiters/{id}/approval")
    public ResponseEntity<Map<String, String>> updateRecruiterApproval(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> payload) {

        if (payload == null || payload.get("isVerified") == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "isVerified is required")
            );
        }

        try {
            adminService.updateRecruiterVerification(
                    id,
                    payload.get("isVerified")
            );

            return ResponseEntity.ok(
                    Map.of("message", "Recruiter verification updated successfully")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "message",
                            e.getMessage() != null
                                    ? e.getMessage()
                                    : "Recruiter company not found"
                    )
            );
        }
    }

    // =========================
    // JOBS
    // =========================

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDto>> getJobs() {
        return ResponseEntity.ok(adminService.getAllJobs());
    }

    @PutMapping("/jobs/{id}/status")
    public ResponseEntity<Map<String, String>> updateJobStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        if (payload == null || payload.get("status") == null
                || payload.get("status").trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Job status is required")
            );
        }

        String status = payload.get("status").trim();

        try {
            adminService.updateJobStatus(id, status);

            return ResponseEntity.ok(
                    Map.of("message", "Job status updated successfully")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "message",
                            e.getMessage() != null
                                    ? e.getMessage()
                                    : "Job not found"
                    )
            );
        }
    }

    // =========================
    // APPLICATIONS
    // =========================

    @GetMapping("/applications")
    public ResponseEntity<List<JobApplicationDto>> getApplications() {
        return ResponseEntity.ok(adminService.getAllApplications());
    }

    // =========================
    // REPORTS
    // =========================

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDto>> getReports() {
        return ResponseEntity.ok(adminService.getAllReports());
    }

    @PutMapping("/reports/{id}/resolve")
    public ResponseEntity<Map<String, String>> resolveReport(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        if (payload == null || payload.get("status") == null
                || payload.get("status").trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Report status is required")
            );
        }

        String status = payload.get("status").trim();
        String notes = payload.get("resolutionNotes");

        try {
            adminService.resolveReport(id, status, notes);

            return ResponseEntity.ok(
                    Map.of("message", "Report resolved successfully")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "message",
                            e.getMessage() != null
                                    ? e.getMessage()
                                    : "Report not found"
                    )
            );
        }
    }
}
