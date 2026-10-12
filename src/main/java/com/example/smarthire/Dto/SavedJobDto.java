package com.example.smarthire.Dto;

import java.time.LocalDateTime;

public class SavedJobDto {
    private Long id;
    private Long jobId;
    private String title;
    private String companyName;
    private String location;
    private String type;
    private String experience;
    private String salary;
    private String skills;
    private Integer eligibilityScore;
    private LocalDateTime savedAt;

    public SavedJobDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public Integer getEligibilityScore() { return eligibilityScore; }
    public void setEligibilityScore(Integer eligibilityScore) { this.eligibilityScore = eligibilityScore; }

    public LocalDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }
}
