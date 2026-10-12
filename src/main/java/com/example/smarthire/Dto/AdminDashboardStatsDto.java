package com.example.smarthire.Dto;

public class AdminDashboardStatsDto {
    private long totalUsers;
    private long totalJobSeekers;
    private long totalRecruiters;
    private long totalJobs;
    private long activeJobs;
    private long totalApplications;
    private long pendingApplications;
    private long pendingReports;

    public AdminDashboardStatsDto() {}

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalJobSeekers() { return totalJobSeekers; }
    public void setTotalJobSeekers(long totalJobSeekers) { this.totalJobSeekers = totalJobSeekers; }

    public long getTotalRecruiters() { return totalRecruiters; }
    public void setTotalRecruiters(long totalRecruiters) { this.totalRecruiters = totalRecruiters; }

    public long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(long totalJobs) { this.totalJobs = totalJobs; }

    public long getActiveJobs() { return activeJobs; }
    public void setActiveJobs(long activeJobs) { this.activeJobs = activeJobs; }

    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }

    public long getPendingApplications() { return pendingApplications; }
    public void setPendingApplications(long pendingApplications) { this.pendingApplications = pendingApplications; }

    public long getPendingReports() { return pendingReports; }
    public void setPendingReports(long pendingReports) { this.pendingReports = pendingReports; }
}
