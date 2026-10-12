package com.example.smarthire.Dto;

import java.time.LocalDateTime;

public class JobDto {
    private Long id;
    private Long recruiterId;
    private String recruiterEmail;
    private String companyName;
    private String title;
    private String location;
    private String type;
    private String workMode;
    private String experience;
    private String education;
    private String skills;
    private String salary;
    private Integer openings;
    private String description;
    private String responsibilities;
    private String requirements;
    private String status;
    private Integer applicantsCount;
    private Integer eligibilityScore; // calculated dynamically for a job seeker
    private LocalDateTime createdAt;

    public JobDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecruiterId() { return recruiterId; }
    public void setRecruiterId(Long recruiterId) { this.recruiterId = recruiterId; }

    public String getRecruiterEmail() { return recruiterEmail; }
    public void setRecruiterEmail(String recruiterEmail) { this.recruiterEmail = recruiterEmail; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getWorkMode() { return workMode; }
    public void setWorkMode(String workMode) { this.workMode = workMode; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public Integer getOpenings() { return openings; }
    public void setOpenings(Integer openings) { this.openings = openings; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getApplicantsCount() { return applicantsCount; }
    public void setApplicantsCount(Integer applicantsCount) { this.applicantsCount = applicantsCount; }

    public Integer getEligibilityScore() { return eligibilityScore; }
    public void setEligibilityScore(Integer eligibilityScore) { this.eligibilityScore = eligibilityScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
