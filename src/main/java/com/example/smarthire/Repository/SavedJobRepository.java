package com.example.smarthire.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.SavedJob;
import com.example.smarthire.Entity.User;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByUserOrderBySavedAtDesc(User user);

    Optional<SavedJob> findByUserAndJob(User user, Job job);

    boolean existsByUserAndJob(User user, Job job);

    void deleteByUserAndJob(User user, Job job);
}
