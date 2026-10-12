package com.example.smarthire;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.smarthire.Dto.JobApplicationDto;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.JobRepository;
import com.example.smarthire.Repository.UserRepository;
import com.example.smarthire.Service.JobApplicationService;

@SpringBootTest
@Transactional
public class JobApplicationFlowTest {

    @Autowired
    private JobApplicationService applicationService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testApplicationFlowAndDuplicatePrevention() {
        User seeker = userRepository.findByRole("JOB_SEEKER").stream().findFirst().orElse(null);
        assertNotNull(seeker, "Job seeker must exist");

        Job activeJob = jobRepository.findByStatusOrderByCreatedAtDesc("Active").stream().findFirst().orElse(null);
        assertNotNull(activeJob, "Active job posting must exist");

        // Submit initial application
        JobApplicationDto app = applicationService.applyForJob(
                seeker.getEmail(),
                activeJob.getId(),
                "Excited to apply for this position!",
                "Resume_Doc.pdf"
        );

        assertNotNull(app.getId());
        assertEquals(activeJob.getId(), app.getJobId());
        assertEquals("Application Submitted", app.getStatus());

        // Attempting to apply again for the same job must throw duplicate application exception
        Exception ex = assertThrows(RuntimeException.class, () -> {
            applicationService.applyForJob(
                    seeker.getEmail(),
                    activeJob.getId(),
                    "Second attempt",
                    "Resume_Doc.pdf"
            );
        });

        assertTrue(ex.getMessage().contains("already submitted an application"));
    }
}
