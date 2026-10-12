package com.example.smarthire.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.smarthire.Dto.JobApplicationDto;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.JobApplication;
import com.example.smarthire.Entity.JobSeekerProfile;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.JobApplicationRepository;
import com.example.smarthire.Repository.JobRepository;
import com.example.smarthire.Repository.JobSeekerProfileRepository;
import com.example.smarthire.Repository.UserRepository;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobSeekerProfileRepository profileRepository;

    @Autowired
    private JobService jobService;

    public JobApplicationDto applyForJob(String applicantEmail, Long jobId, String coverMessage, String resumePath) {
        User applicant = userRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"JOB_SEEKER".equals(applicant.getRole())) {
            throw new RuntimeException("Only job seekers can apply for jobs");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!"Active".equalsIgnoreCase(job.getStatus())) {
            throw new RuntimeException("This job is no longer accepting applications");
        }

        if (applicationRepository.existsByApplicantAndJob(applicant, job)) {
            throw new RuntimeException("You have already submitted an application for this position");
        }

        int score = jobService.calculateEligibility(job, applicantEmail);

        JobApplication app = new JobApplication();
        app.setJob(job);
        app.setApplicant(applicant);
        app.setCoverMessage(coverMessage);
        app.setResumePath(resumePath != null ? resumePath : "Resume_Attached.pdf");
        app.setStatus("Application Submitted");
        app.setEligibilityScore(score);

        JobApplication saved = applicationRepository.save(app);
        return convertToDto(saved);
    }

    public List<JobApplicationDto> getSeekerApplications(String seekerEmail) {
        User applicant = userRepository.findByEmail(seekerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository.findByApplicantOrderByAppliedAtDesc(applicant)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public JobApplicationDto getApplicationById(Long applicationId, String userEmail) {
        JobApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isApplicant = app.getApplicant().getId().equals(user.getId());
        boolean isRecruiter = app.getJob().getRecruiter().getId().equals(user.getId());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if (!isApplicant && !isRecruiter && !isAdmin) {
            throw new RuntimeException("You are not authorized to view this application");
        }

        return convertToDto(app);
    }

    public List<JobApplicationDto> getRecruiterApplications(String recruiterEmail, Long jobIdOpt) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<JobApplication> list;
        if (jobIdOpt != null) {
            Job job = jobRepository.findById(jobIdOpt)
                    .orElseThrow(() -> new RuntimeException("Job not found"));
            if (!job.getRecruiter().getId().equals(recruiter.getId()) && !"ADMIN".equals(recruiter.getRole())) {
                throw new RuntimeException("Permission denied");
            }
            list = applicationRepository.findByJobOrderByAppliedAtDesc(job);
        } else {
            list = applicationRepository.findByJob_RecruiterOrderByAppliedAtDesc(recruiter);
        }

        return list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public JobApplicationDto updateApplicationStatus(String recruiterEmail, Long applicationId, String newStatus) {
        JobApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        User user = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isRecruiter = app.getJob().getRecruiter().getId().equals(user.getId());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if (!isRecruiter && !isAdmin) {
            throw new RuntimeException("You are not authorized to change the status of this application");
        }

        app.setStatus(newStatus);
        JobApplication saved = applicationRepository.save(app);
        return convertToDto(saved);
    }

    public JobApplicationDto convertToDto(JobApplication app) {
        JobApplicationDto dto = new JobApplicationDto();
        dto.setId(app.getId());
        dto.setJobId(app.getJob().getId());
        dto.setJobTitle(app.getJob().getTitle());
        dto.setCompanyName(app.getJob().getCompanyName());
        dto.setJobLocation(app.getJob().getLocation());
        dto.setApplicantId(app.getApplicant().getId());
        dto.setApplicantName(app.getApplicant().getFullName());
        dto.setApplicantEmail(app.getApplicant().getEmail());
        dto.setApplicantPhone(app.getApplicant().getPhone());
        dto.setCoverMessage(app.getCoverMessage());
        dto.setResumePath(app.getResumePath());
        dto.setStatus(app.getStatus());
        dto.setEligibilityScore(app.getEligibilityScore());
        dto.setAppliedAt(app.getAppliedAt());

        JobSeekerProfile profile = profileRepository.findByUser(app.getApplicant()).orElse(null);
        if (profile != null) {
            dto.setApplicantSkills(profile.getSkills());
            dto.setApplicantEducation(profile.getDegree() != null ? profile.getDegree() + " (" + profile.getCollege() + ")" : profile.getQualification());
            dto.setApplicantExperience(profile.getExperienceType() != null ? profile.getExperienceType() + " (" + profile.getExperienceYears() + " yrs)" : "Fresher");
        }
        return dto;
    }
}
