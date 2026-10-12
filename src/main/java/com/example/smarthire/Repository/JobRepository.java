package com.example.smarthire.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.User;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiterOrderByCreatedAtDesc(User recruiter);

    List<Job> findByStatusOrderByCreatedAtDesc(String status);

    long countByStatus(String status);

    @Query("SELECT j FROM Job j WHERE j.status = 'Active' AND (" +
           "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.skills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
           ") ORDER BY j.createdAt DESC")
    List<Job> searchActiveJobs(@Param("keyword") String keyword);
}
