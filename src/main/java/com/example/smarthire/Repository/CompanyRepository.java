package com.example.smarthire.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.smarthire.Entity.Company;
import com.example.smarthire.Entity.User;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUser(User user);

    Optional<Company> findByUserId(Long userId);

    boolean existsByUser(User user);
}
