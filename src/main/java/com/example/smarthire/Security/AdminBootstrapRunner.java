package com.example.smarthire.Security;

import java.io.File;
import java.io.FileWriter;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.smarthire.Entity.Company;
import com.example.smarthire.Entity.Job;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.CompanyRepository;
import com.example.smarthire.Repository.JobRepository;
import com.example.smarthire.Repository.UserRepository;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        provisionAdminIfNotExists();
        provisionSampleJobsIfNotExists();
    }

    private void provisionAdminIfNotExists() {
        String envEmail = System.getenv("SMARTHIRE_ADMIN_EMAIL");
        String envPassword = System.getenv("SMARTHIRE_ADMIN_PASSWORD");

        String adminEmail = (envEmail != null && envEmail.trim().contains("@"))
                ? envEmail.trim()
                : "admin@smarthire.local";

        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);
        if (existingAdmin.isEmpty()) {
            existingAdmin = userRepository.findAll().stream()
                    .filter(u -> "ADMIN".equals(u.getRole()))
                    .findFirst();
        }

        if (existingAdmin.isEmpty()) {
            String adminPassword;
            boolean generated = false;

            if (envPassword != null && envPassword.trim().length() >= 8) {
                adminPassword = envPassword.trim();
            } else {
                byte[] randomBytes = new byte[12];
                new SecureRandom().nextBytes(randomBytes);
                adminPassword = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
                generated = true;
            }

            User admin = new User(
                    "System Administrator",
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    "+91 9999999999",
                    "ADMIN"
            );
            admin.setStatus("ACTIVE");
            userRepository.save(admin);

            if (generated) {
                saveSecretToFile(adminEmail, adminPassword);
            }
        } else if (envPassword != null && envPassword.trim().length() >= 8) {
            User admin = existingAdmin.get();
            admin.setPassword(passwordEncoder.encode(envPassword.trim()));
            userRepository.save(admin);
            logger.info("Admin credential synchronized securely via SMARTHIRE_ADMIN_PASSWORD environment variable.");
        } else {
            File credFile = new File("d:/SmartHire/_backups/admin_bootstrap_secret.txt");
            if (!credFile.exists()) {
                byte[] randomBytes = new byte[12];
                new SecureRandom().nextBytes(randomBytes);
                String adminPassword = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
                User admin = existingAdmin.get();
                admin.setPassword(passwordEncoder.encode(adminPassword));
                userRepository.save(admin);
                saveSecretToFile(admin.getEmail(), adminPassword);
            }
        }
    }

    private void saveSecretToFile(String adminEmail, String adminPassword) {
        try {
            File credDir = new File("d:/SmartHire/_backups");
            if (!credDir.exists()) {
                credDir.mkdirs();
            }
            File credFile = new File(credDir, "admin_bootstrap_secret.txt");
            try (FileWriter writer = new FileWriter(credFile, false)) {
                writer.write("SMARTHIRE SECURE INITIAL ADMIN PROVISIONING\n");
                writer.write("Timestamp: " + java.time.Instant.now() + "\n");
                writer.write("Admin Email: " + adminEmail + "\n");
                writer.write("One-Time Password: " + adminPassword + "\n");
                writer.write("NOTICE: Rotate this password and delete this file after updating the admin credentials.\n");
            }
            logger.info("Admin account provisioned securely. One-time bootstrap credential stored in _backups/admin_bootstrap_secret.txt (never logged to console).");
        } catch (Exception ex) {
            logger.warn("Admin provisioned but could not write credential file: {}", ex.getMessage());
        }
    }

    private void provisionSampleJobsIfNotExists() {
        if (jobRepository.count() == 0) {
            User recruiter = userRepository.findByRole("RECRUITER").stream().findFirst().orElse(null);
            if (recruiter != null) {
                Company company = companyRepository.findByUser(recruiter).orElseGet(() -> {
                    Company c = new Company();
                    c.setUser(recruiter);
                    c.setCompanyName("TechNova Solutions");
                    c.setIndustry("Information Technology");
                    c.setLocation("Hyderabad, Telangana");
                    c.setCompanySize("51 - 200 Employees");
                    c.setWebsite("https://technovasolutions.com");
                    c.setDescription("Leading enterprise cloud and software solutions provider.");
                    return companyRepository.save(c);
                });

                Job job1 = new Job();
                job1.setRecruiter(recruiter);
                job1.setCompanyName(company.getCompanyName());
                job1.setTitle("Software Developer");
                job1.setLocation("Hyderabad, Telangana");
                job1.setType("Full Time");
                job1.setWorkMode("Hybrid");
                job1.setExperience("0-2 Years");
                job1.setEducation("B.Tech / B.E in Computer Science");
                job1.setSkills("Java, Spring Boot, MySQL, React");
                job1.setSalary("₹5 - ₹8 LPA");
                job1.setOpenings(3);
                job1.setDescription("Looking for a passionate Software Developer to build scalable full-stack applications.");
                job1.setResponsibilities("Develop robust backend services with Spring Boot\nBuild responsive UI with React\nCollaborate on database schema design");
                job1.setRequirements("Strong foundation in Java and Object Oriented Programming\nExperience with relational databases\nFamiliarity with Git and REST APIs");
                job1.setStatus("Active");
                jobRepository.save(job1);

                Job job2 = new Job();
                job2.setRecruiter(recruiter);
                job2.setCompanyName(company.getCompanyName());
                job2.setTitle("Frontend Developer");
                job2.setLocation("Remote");
                job2.setType("Full Time");
                job2.setWorkMode("Remote");
                job2.setExperience("1-3 Years");
                job2.setEducation("B.Tech / BCA / MCA");
                job2.setSkills("React, JavaScript, CSS, HTML");
                job2.setSalary("₹6 - ₹9 LPA");
                job2.setOpenings(2);
                job2.setDescription("Create responsive, modern and high performance user interfaces using React.");
                job2.setResponsibilities("Build interactive UI components\nOptimize web applications for performance\nIntegrate RESTful APIs");
                job2.setRequirements("Deep understanding of modern JavaScript (ES6+)\nProficiency in React and state management\nEye for design and accessibility");
                job2.setStatus("Active");
                jobRepository.save(job2);

                logger.info("Sample active jobs seeded for recruiter: {}", recruiter.getEmail());
            }
        }
    }
}
