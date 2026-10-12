package com.example.smarthire.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Boolean emailNotifications = true;
    private Boolean applicationUpdates = true;
    private Boolean jobAlerts = true;
    private Boolean platformAnnouncements = true;
    private Boolean darkMode = true;
    private Boolean reducedMotion = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserSettings() {}

    public UserSettings(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getApplicationUpdates() {
        return applicationUpdates;
    }

    public void setApplicationUpdates(Boolean applicationUpdates) {
        this.applicationUpdates = applicationUpdates;
    }

    public Boolean getJobAlerts() {
        return jobAlerts;
    }

    public void setJobAlerts(Boolean jobAlerts) {
        this.jobAlerts = jobAlerts;
    }

    public Boolean getPlatformAnnouncements() {
        return platformAnnouncements;
    }

    public void setPlatformAnnouncements(Boolean platformAnnouncements) {
        this.platformAnnouncements = platformAnnouncements;
    }

    public Boolean getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
    }

    public Boolean getReducedMotion() {
        return reducedMotion;
    }

    public void setReducedMotion(Boolean reducedMotion) {
        this.reducedMotion = reducedMotion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
