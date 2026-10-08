package com.example.smarthire.Repository;

import com.example.smarthire.Entity.JobSeekerProfile;
import com.example.smarthire.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerProfileRepository
        extends JpaRepository<JobSeekerProfile, Long> {

    Optional<JobSeekerProfile> findByUser(User user);
}