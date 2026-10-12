package com.example.smarthire.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.smarthire.Dto.SavedJobDto;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.SavedJob;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.JobRepository;
import com.example.smarthire.Repository.SavedJobRepository;
import com.example.smarthire.Repository.UserRepository;

@Service
public class SavedJobService {

    @Autowired
    private SavedJobRepository savedJobRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobService jobService;

    public SavedJobDto saveJob(String userEmail, Long jobId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (savedJobRepository.existsByUserAndJob(user, job)) {
            SavedJob existing = savedJobRepository.findByUserAndJob(user, job).get();
            return convertToDto(existing, userEmail);
        }

        SavedJob savedJob = new SavedJob(user, job);
        SavedJob saved = savedJobRepository.save(savedJob);
        return convertToDto(saved, userEmail);
    }

    @Transactional
    public void unsaveJob(String userEmail, Long jobId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        savedJobRepository.deleteByUserAndJob(user, job);
    }

    public List<SavedJobDto> getSavedJobs(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savedJobRepository.findByUserOrderBySavedAtDesc(user)
                .stream()
                .map(sj -> convertToDto(sj, userEmail))
                .collect(Collectors.toList());
    }

    public boolean isJobSaved(String userEmail, Long jobId) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) return false;
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) return false;
        return savedJobRepository.existsByUserAndJob(user, job);
    }

    private SavedJobDto convertToDto(SavedJob savedJob, String userEmail) {
        SavedJobDto dto = new SavedJobDto();
        dto.setId(savedJob.getId());
        dto.setJobId(savedJob.getJob().getId());
        dto.setTitle(savedJob.getJob().getTitle());
        dto.setCompanyName(savedJob.getJob().getCompanyName());
        dto.setLocation(savedJob.getJob().getLocation());
        dto.setType(savedJob.getJob().getType());
        dto.setExperience(savedJob.getJob().getExperience());
        dto.setSalary(savedJob.getJob().getSalary());
        dto.setSkills(savedJob.getJob().getSkills());
        dto.setEligibilityScore(jobService.calculateEligibility(savedJob.getJob(), userEmail));
        dto.setSavedAt(savedJob.getSavedAt());
        return dto;
    }
}
