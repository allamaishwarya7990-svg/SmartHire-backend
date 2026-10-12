package com.example.smarthire.Service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.smarthire.Dto.JobDto;
import com.example.smarthire.Entity.Company;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.JobSeekerProfile;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.CompanyRepository;
import com.example.smarthire.Repository.JobApplicationRepository;
import com.example.smarthire.Repository.JobRepository;
import com.example.smarthire.Repository.JobSeekerProfileRepository;
import com.example.smarthire.Repository.UserRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private JobSeekerProfileRepository profileRepository;

    public JobDto createJob(String recruiterEmail, JobDto dto) {
        User user = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"RECRUITER".equals(user.getRole())) {
            throw new RuntimeException("Only recruiters can post jobs");
        }

        Company company = companyRepository.findByUser(user).orElse(null);
        String compName = dto.getCompanyName();
        if (compName == null || compName.trim().isEmpty()) {
            compName = (company != null && company.getCompanyName() != null)
                    ? company.getCompanyName()
                    : (user.getFullName() != null ? user.getFullName() + " Solutions" : "Company");
        }

        Job job = new Job();
        job.setRecruiter(user);
        job.setCompanyName(compName);
        job.setTitle(dto.getTitle());
        job.setLocation(dto.getLocation());
        job.setType(dto.getType() != null ? dto.getType() : "Full Time");
        job.setWorkMode(dto.getWorkMode() != null ? dto.getWorkMode() : "On-site");
        job.setExperience(dto.getExperience() != null ? dto.getExperience() : "0-2 Years");
        job.setEducation(dto.getEducation() != null ? dto.getEducation() : "Any Graduate");
        job.setSkills(dto.getSkills() != null ? dto.getSkills() : "Java, React, SQL");
        job.setSalary(dto.getSalary() != null ? dto.getSalary() : "Competitive");
        job.setOpenings(dto.getOpenings() != null ? dto.getOpenings() : 1);
        job.setDescription(dto.getDescription() != null ? dto.getDescription() : dto.getTitle());
        job.setResponsibilities(dto.getResponsibilities());
        job.setRequirements(dto.getRequirements());
        job.setStatus("Active");

        Job saved = jobRepository.save(job);
        return convertToDto(saved, recruiterEmail);
    }

    public JobDto updateJob(String recruiterEmail, Long jobId, JobDto dto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getEmail().equals(recruiterEmail)) {
            User current = userRepository.findByEmail(recruiterEmail).orElse(null);
            if (current == null || !"ADMIN".equals(current.getRole())) {
                throw new RuntimeException("You do not have permission to modify this job");
            }
        }

        if (dto.getTitle() != null) job.setTitle(dto.getTitle());
        if (dto.getCompanyName() != null) job.setCompanyName(dto.getCompanyName());
        if (dto.getLocation() != null) job.setLocation(dto.getLocation());
        if (dto.getType() != null) job.setType(dto.getType());
        if (dto.getWorkMode() != null) job.setWorkMode(dto.getWorkMode());
        if (dto.getExperience() != null) job.setExperience(dto.getExperience());
        if (dto.getEducation() != null) job.setEducation(dto.getEducation());
        if (dto.getSkills() != null) job.setSkills(dto.getSkills());
        if (dto.getSalary() != null) job.setSalary(dto.getSalary());
        if (dto.getOpenings() != null) job.setOpenings(dto.getOpenings());
        if (dto.getDescription() != null) job.setDescription(dto.getDescription());
        if (dto.getResponsibilities() != null) job.setResponsibilities(dto.getResponsibilities());
        if (dto.getRequirements() != null) job.setRequirements(dto.getRequirements());
        if (dto.getStatus() != null) job.setStatus(dto.getStatus());

        Job saved = jobRepository.save(job);
        return convertToDto(saved, recruiterEmail);
    }

    public JobDto closeJob(String recruiterEmail, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getEmail().equals(recruiterEmail)) {
            User current = userRepository.findByEmail(recruiterEmail).orElse(null);
            if (current == null || !"ADMIN".equals(current.getRole())) {
                throw new RuntimeException("Permission denied");
            }
        }

        job.setStatus("Closed");
        return convertToDto(jobRepository.save(job), recruiterEmail);
    }

    public JobDto reopenJob(String recruiterEmail, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getEmail().equals(recruiterEmail)) {
            User current = userRepository.findByEmail(recruiterEmail).orElse(null);
            if (current == null || !"ADMIN".equals(current.getRole())) {
                throw new RuntimeException("Permission denied");
            }
        }

        job.setStatus("Active");
        return convertToDto(jobRepository.save(job), recruiterEmail);
    }

    public void deleteJob(String userEmail, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        User current = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!job.getRecruiter().getEmail().equals(userEmail) && !"ADMIN".equals(current.getRole())) {
            throw new RuntimeException("Permission denied");
        }

        jobRepository.delete(job);
    }

    public List<JobDto> getRecruiterJobs(String recruiterEmail) {
        User user = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Job> jobs = jobRepository.findByRecruiterOrderByCreatedAtDesc(user);
        return jobs.stream()
                .map(j -> convertToDto(j, recruiterEmail))
                .collect(Collectors.toList());
    }

    public JobDto getJobById(Long jobId, String userEmailOpt) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return convertToDto(job, userEmailOpt);
    }

    public List<JobDto> searchJobs(String keyword, String location, String jobType, String experience, String userEmailOpt) {
        List<Job> jobs;
        if (keyword != null && !keyword.trim().isEmpty()) {
            jobs = jobRepository.searchActiveJobs(keyword.trim());
        } else {
            jobs = jobRepository.findByStatusOrderByCreatedAtDesc("Active");
        }

        // Apply in-memory filters for location, jobType, experience
        return jobs.stream().filter(j -> {
            if (location != null && !location.trim().isEmpty() && !location.equalsIgnoreCase("All")) {
                if (!j.getLocation().toLowerCase().contains(location.toLowerCase().trim())) {
                    return false;
                }
            }
            if (jobType != null && !jobType.trim().isEmpty() && !jobType.equalsIgnoreCase("All")) {
                if (!j.getType().equalsIgnoreCase(jobType.trim())) {
                    return false;
                }
            }
            if (experience != null && !experience.trim().isEmpty() && !experience.equalsIgnoreCase("All")) {
                if (!j.getExperience().toLowerCase().contains(experience.toLowerCase().trim())) {
                    return false;
                }
            }
            return true;
        }).map(j -> convertToDto(j, userEmailOpt))
          .collect(Collectors.toList());
    }

    public int calculateEligibility(Job job, String seekerEmail) {
        if (seekerEmail == null || seekerEmail.isEmpty()) {
            return 75; // default benchmark
        }
        User user = userRepository.findByEmail(seekerEmail).orElse(null);
        if (user == null || !"JOB_SEEKER".equals(user.getRole())) {
            return 75;
        }

        JobSeekerProfile profile = profileRepository.findByUser(user).orElse(null);
        if (profile == null || profile.getSkills() == null || profile.getSkills().trim().isEmpty()) {
            return 50;
        }

        Set<String> candidateSkills = Arrays.stream(profile.getSkills().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        Set<String> requiredSkills = Arrays.stream(job.getSkills().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        if (requiredSkills.isEmpty()) {
            return 80;
        }

        long matched = requiredSkills.stream().filter(candidateSkills::contains).count();
        double matchRatio = (double) matched / requiredSkills.size();

        int score = (int) Math.round(matchRatio * 60) + 40; // baseline 40% with skills contributing up to 60%
        return Math.min(100, Math.max(40, score));
    }

    public JobDto convertToDto(Job job, String userEmailOpt) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setRecruiterId(job.getRecruiter().getId());
        dto.setRecruiterEmail(job.getRecruiter().getEmail());
        dto.setCompanyName(job.getCompanyName());
        dto.setTitle(job.getTitle());
        dto.setLocation(job.getLocation());
        dto.setType(job.getType());
        dto.setWorkMode(job.getWorkMode());
        dto.setExperience(job.getExperience());
        dto.setEducation(job.getEducation());
        dto.setSkills(job.getSkills());
        dto.setSalary(job.getSalary());
        dto.setOpenings(job.getOpenings());
        dto.setDescription(job.getDescription());
        dto.setResponsibilities(job.getResponsibilities());
        dto.setRequirements(job.getRequirements());
        dto.setStatus(job.getStatus());
        dto.setCreatedAt(job.getCreatedAt());

        long count = applicationRepository.countByJob(job);
        dto.setApplicantsCount((int) count);

        dto.setEligibilityScore(calculateEligibility(job, userEmailOpt));
        return dto;
    }
}
