package com.example.smarthire.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.smarthire.Entity.Report;
import com.example.smarthire.Entity.User;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByOrderByCreatedAtDesc();

    List<Report> findByReporterOrderByCreatedAtDesc(User reporter);

    List<Report> findByStatusOrderByCreatedAtDesc(String status);

    long countByStatus(String status);
}
