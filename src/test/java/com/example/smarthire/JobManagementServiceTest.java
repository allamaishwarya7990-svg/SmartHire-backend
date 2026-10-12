package com.example.smarthire;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.smarthire.Dto.JobDto;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.JobRepository;
import com.example.smarthire.Repository.UserRepository;
import com.example.smarthire.Service.JobService;

@SpringBootTest
@Transactional
public class JobManagementServiceTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testJobCreationAndEligibilityCalculation() {
        User recruiter = userRepository.findByRole("RECRUITER").stream().findFirst().orElse(null);
        assertNotNull(recruiter, "Recruiter must exist in database");

        JobDto newJob = new JobDto();
        newJob.setTitle("Cloud Architect");
        newJob.setCompanyName("TechNova Solutions");
        newJob.setLocation("Hyderabad, Telangana");
        newJob.setType("Full Time");
        newJob.setWorkMode("Remote");
        newJob.setSkills("Java, AWS, Kubernetes, Spring Boot");
        newJob.setSalary("₹18 - ₹25 LPA");
        newJob.setExperience("3-5 Years");
        newJob.setEducation("B.Tech Computer Science");
        newJob.setDescription("Architect next-generation cloud infrastructure.");

        JobDto created = jobService.createJob(recruiter.getEmail(), newJob);
        assertNotNull(created.getId());
        assertEquals("Cloud Architect", created.getTitle());
        assertEquals("Active", created.getStatus());

        // Test eligibility calculation
        Job entity = jobRepository.findById(created.getId()).orElse(null);
        assertNotNull(entity);
        int score = jobService.calculateEligibility(entity, "dummy@candidate.com");
        assertTrue(score >= 40 && score <= 100);
    }

    @Test
    public void testJobOwnershipEnforcement() {
        User recruiter = userRepository.findByRole("RECRUITER").stream().findFirst().orElse(null);
        assertNotNull(recruiter);

        List<JobDto> jobs = jobService.getRecruiterJobs(recruiter.getEmail());
        if (!jobs.isEmpty()) {
            Long jobId = jobs.get(0).getId();

            // Attempting to close with unauthorized non-owner email must throw exception
            assertThrows(RuntimeException.class, () -> {
                jobService.closeJob("unauthorized.intruder@example.com", jobId);
            });
        }
    }
}
