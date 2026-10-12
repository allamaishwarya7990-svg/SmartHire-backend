package com.example.smarthire.Dto;

import java.time.LocalDateTime;

public class ReportDto {
    private Long id;
    private Long reporterId;
    private String reporterEmail;
    private String targetType;
    private Long targetId;
    private String targetTitle;
    private String reason;
    private String description;
    private String status;
    private String resolutionNotes;
    private LocalDateTime createdAt;

    public ReportDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReporterId() { return reporterId; }
    public void setReporterId(Long reporterId) { this.reporterId = reporterId; }

    public String getReporterEmail() { return reporterEmail; }
    public void setReporterEmail(String reporterEmail) { this.reporterEmail = reporterEmail; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public String getTargetTitle() { return targetTitle; }
    public void setTargetTitle(String targetTitle) { this.targetTitle = targetTitle; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
