package com.example.smarthire.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.JobApplication;
import com.example.smarthire.Entity.User;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByApplicantOrderByAppliedAtDesc(User applicant);

    List<JobApplication> findByJobOrderByAppliedAtDesc(Job job);

    List<JobApplication> findByJob_RecruiterOrderByAppliedAtDesc(User recruiter);

    Optional<JobApplication> findByApplicantAndJob(User applicant, Job job);

    boolean existsByApplicantAndJob(User applicant, Job job);

    long countByJob(Job job);

    long countByJob_Recruiter(User recruiter);

    long countByStatus(String status);
}
