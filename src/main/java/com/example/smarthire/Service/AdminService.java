package com.example.smarthire.Service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.smarthire.Dto.AdminDashboardStatsDto;
import com.example.smarthire.Dto.JobApplicationDto;
import com.example.smarthire.Dto.JobDto;
import com.example.smarthire.Dto.ReportDto;
import com.example.smarthire.Entity.*;
import com.example.smarthire.Repository.*;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobApplicationService applicationService;

    public AdminDashboardStatsDto getDashboardStats() {
        AdminDashboardStatsDto stats = new AdminDashboardStatsDto();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalJobSeekers(userRepository.countByRole("JOB_SEEKER"));
        stats.setTotalRecruiters(userRepository.countByRole("RECRUITER"));
        stats.setTotalJobs(jobRepository.count());
        stats.setActiveJobs(jobRepository.countByStatus("Active"));
        stats.setTotalApplications(applicationRepository.count());
        stats.setPendingApplications(applicationRepository.countByStatus("Application Submitted") +
                                     applicationRepository.countByStatus("Under Review"));
        stats.setPendingReports(reportRepository.countByStatus("Pending"));
        return stats;
    }

    public List<Map<String, Object>> getAllJobSeekers() {
        return userRepository.findByRole("JOB_SEEKER").stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("fullName", u.getFullName());
            map.put("email", u.getEmail());
            map.put("phone", u.getPhone());
            map.put("role", u.getRole());
            map.put("status", u.getStatus() != null ? u.getStatus() : "ACTIVE");
            return map;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllRecruiters() {
        return userRepository.findByRole("RECRUITER").stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("fullName", u.getFullName());
            map.put("email", u.getEmail());
            map.put("phone", u.getPhone());
            map.put("role", u.getRole());
            map.put("status", u.getStatus() != null ? u.getStatus() : "ACTIVE");

            Company comp = companyRepository.findByUser(u).orElse(null);
            if (comp != null) {
                map.put("companyName", comp.getCompanyName());
                map.put("industry", comp.getIndustry());
                map.put("location", comp.getLocation());
                map.put("isVerified", comp.getIsVerified());
            } else {
                map.put("companyName", "N/A");
                map.put("industry", "N/A");
                map.put("location", "N/A");
                map.put("isVerified", false);
            }
            return map;
        }).collect(Collectors.toList());
    }

    public void updateUserStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public void updateRecruiterVerification(Long recruiterId, Boolean isVerified) {
        User user = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        Company comp = companyRepository.findByUser(user).orElseGet(() -> {
            Company c = new Company();
            c.setUser(user);
            c.setCompanyName(user.getFullName() + " Company");
            return c;
        });
        comp.setIsVerified(isVerified);
        companyRepository.save(comp);
    }

    public List<JobDto> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(j -> jobService.convertToDto(j, null))
                .collect(Collectors.toList());
    }

    public void updateJobStatus(Long jobId, String status) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        jobRepository.save(job);
    }

    public List<JobApplicationDto> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(applicationService::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReportDto> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertReportToDto)
                .collect(Collectors.toList());
    }

    public void resolveReport(Long reportId, String status, String notes) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(status);
        report.setResolutionNotes(notes);
        reportRepository.save(report);
    }

    private ReportDto convertReportToDto(Report r) {
        ReportDto dto = new ReportDto();
        dto.setId(r.getId());
        dto.setReporterId(r.getReporter().getId());
        dto.setReporterEmail(r.getReporter().getEmail());
        dto.setTargetType(r.getTargetType());
        dto.setTargetId(r.getTargetId());
        dto.setTargetTitle(r.getTargetTitle());
        dto.setReason(r.getReason());
        dto.setDescription(r.getDescription());
        dto.setStatus(r.getStatus());
        dto.setResolutionNotes(r.getResolutionNotes());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
