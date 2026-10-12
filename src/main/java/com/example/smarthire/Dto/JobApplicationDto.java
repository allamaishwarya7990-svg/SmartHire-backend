package com.example.smarthire.Dto;

import java.time.LocalDateTime;

public class JobApplicationDto {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String jobLocation;
    private Long applicantId;
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private String applicantSkills;
    private String applicantEducation;
    private String applicantExperience;
    private String coverMessage;
    private String resumePath;
    private String status;
    private Integer eligibilityScore;
    private LocalDateTime appliedAt;

    public JobApplicationDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobLocation() { return jobLocation; }
    public void setJobLocation(String jobLocation) { this.jobLocation = jobLocation; }

    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getApplicantEmail() { return applicantEmail; }
    public void setApplicantEmail(String applicantEmail) { this.applicantEmail = applicantEmail; }

    public String getApplicantPhone() { return applicantPhone; }
    public void setApplicantPhone(String applicantPhone) { this.applicantPhone = applicantPhone; }

    public String getApplicantSkills() { return applicantSkills; }
    public void setApplicantSkills(String applicantSkills) { this.applicantSkills = applicantSkills; }

    public String getApplicantEducation() { return applicantEducation; }
    public void setApplicantEducation(String applicantEducation) { this.applicantEducation = applicantEducation; }

    public String getApplicantExperience() { return applicantExperience; }
    public void setApplicantExperience(String applicantExperience) { this.applicantExperience = applicantExperience; }

    public String getCoverMessage() { return coverMessage; }
    public void setCoverMessage(String coverMessage) { this.coverMessage = coverMessage; }

    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getEligibilityScore() { return eligibilityScore; }
    public void setEligibilityScore(Integer eligibilityScore) { this.eligibilityScore = eligibilityScore; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
