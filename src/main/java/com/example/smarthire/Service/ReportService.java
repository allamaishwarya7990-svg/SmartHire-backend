package com.example.smarthire.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.smarthire.Dto.ReportDto;
import com.example.smarthire.Entity.Report;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.ReportRepository;
import com.example.smarthire.Repository.UserRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    public ReportDto submitReport(String reporterEmail, ReportDto dto) {
        User user = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = new Report();
        report.setReporter(user);
        report.setTargetType(dto.getTargetType() != null ? dto.getTargetType() : "JOB");
        report.setTargetId(dto.getTargetId() != null ? dto.getTargetId() : 0L);
        report.setTargetTitle(dto.getTargetTitle());
        report.setReason(dto.getReason() != null ? dto.getReason() : "Inappropriate content");
        report.setDescription(dto.getDescription());
        report.setStatus("Pending");

        Report saved = reportRepository.save(report);
        return convertToDto(saved);
    }

    public List<ReportDto> getUserReports(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reportRepository.findByReporterOrderByCreatedAtDesc(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReportDto convertToDto(Report r) {
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
