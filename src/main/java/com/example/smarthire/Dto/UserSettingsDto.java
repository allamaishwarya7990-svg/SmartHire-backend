package com.example.smarthire.Dto;

public class UserSettingsDto {
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String status;
    private Boolean emailNotifications;
    private Boolean applicationUpdates;
    private Boolean jobAlerts;
    private Boolean platformAnnouncements;
    private Boolean darkMode;
    private Boolean reducedMotion;

    public UserSettingsDto() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(Boolean emailNotifications) { this.emailNotifications = emailNotifications; }

    public Boolean getApplicationUpdates() { return applicationUpdates; }
    public void setApplicationUpdates(Boolean applicationUpdates) { this.applicationUpdates = applicationUpdates; }

    public Boolean getJobAlerts() { return jobAlerts; }
    public void setJobAlerts(Boolean jobAlerts) { this.jobAlerts = jobAlerts; }

    public Boolean getPlatformAnnouncements() { return platformAnnouncements; }
    public void setPlatformAnnouncements(Boolean platformAnnouncements) { this.platformAnnouncements = platformAnnouncements; }

    public Boolean getDarkMode() { return darkMode; }
    public void setDarkMode(Boolean darkMode) { this.darkMode = darkMode; }

    public Boolean getReducedMotion() { return reducedMotion; }
    public void setReducedMotion(Boolean reducedMotion) { this.reducedMotion = reducedMotion; }
}
