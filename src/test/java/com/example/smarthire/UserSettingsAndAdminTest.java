package com.example.smarthire;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.smarthire.Dto.AdminDashboardStatsDto;
import com.example.smarthire.Dto.UserSettingsDto;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.UserRepository;
import com.example.smarthire.Service.AdminService;
import com.example.smarthire.Service.UserSettingsService;

@SpringBootTest
@Transactional
public class UserSettingsAndAdminTest {

    @Autowired
    private UserSettingsService settingsService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserSettingsRetrievalAndPersistence() {
        User user = userRepository.findByRole("JOB_SEEKER").stream().findFirst().orElse(null);
        assertNotNull(user);

        UserSettingsDto initial = settingsService.getUserSettings(user.getEmail());
        assertNotNull(initial);
        assertTrue(initial.getDarkMode());

        initial.setJobAlerts(false);
        UserSettingsDto updated = settingsService.updateUserSettings(user.getEmail(), initial);
        assertFalse(updated.getJobAlerts());
    }

    @Test
    public void testAdminDashboardStats() {
        AdminDashboardStatsDto stats = adminService.getDashboardStats();
        assertNotNull(stats);
        assertTrue(stats.getTotalJobSeekers() >= 1);
        assertTrue(stats.getTotalRecruiters() >= 1);
        assertTrue(stats.getTotalJobs() >= 1);
    }
}
