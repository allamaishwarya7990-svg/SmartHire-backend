package com.example.smarthire.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.smarthire.Dto.PasswordChangeRequest;
import com.example.smarthire.Dto.UserSettingsDto;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Entity.UserSettings;
import com.example.smarthire.Repository.UserRepository;
import com.example.smarthire.Repository.UserSettingsRepository;

@Service
public class UserSettingsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSettingsRepository settingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserSettingsDto getUserSettings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserSettings settings = settingsRepository.findByUser(user)
                .orElseGet(() -> settingsRepository.save(new UserSettings(user)));

        return convertToDto(user, settings);
    }

    public UserSettingsDto updateUserSettings(String userEmail, UserSettingsDto dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFullName() != null && !dto.getFullName().trim().isEmpty()) {
            user.setFullName(dto.getFullName().trim());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone().trim());
        }
        userRepository.save(user);

        UserSettings settings = settingsRepository.findByUser(user)
                .orElseGet(() -> new UserSettings(user));

        if (dto.getEmailNotifications() != null) settings.setEmailNotifications(dto.getEmailNotifications());
        if (dto.getApplicationUpdates() != null) settings.setApplicationUpdates(dto.getApplicationUpdates());
        if (dto.getJobAlerts() != null) settings.setJobAlerts(dto.getJobAlerts());
        if (dto.getPlatformAnnouncements() != null) settings.setPlatformAnnouncements(dto.getPlatformAnnouncements());
        if (dto.getDarkMode() != null) settings.setDarkMode(dto.getDarkMode());
        if (dto.getReducedMotion() != null) settings.setReducedMotion(dto.getReducedMotion());

        UserSettings saved = settingsRepository.save(settings);
        return convertToDto(user, saved);
    }

    public void changePassword(String userEmail, PasswordChangeRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getCurrentPassword() == null || request.getNewPassword() == null) {
            throw new RuntimeException("Both current password and new password are required");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password does not match our records");
        }

        if (request.getNewPassword().trim().length() < 6) {
            throw new RuntimeException("New password must be at least 6 characters long");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword().trim()));
        userRepository.save(user);
    }

    private UserSettingsDto convertToDto(User user, UserSettings settings) {
        UserSettingsDto dto = new UserSettingsDto();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());

        dto.setEmailNotifications(settings.getEmailNotifications());
        dto.setApplicationUpdates(settings.getApplicationUpdates());
        dto.setJobAlerts(settings.getJobAlerts());
        dto.setPlatformAnnouncements(settings.getPlatformAnnouncements());
        dto.setDarkMode(settings.getDarkMode());
        dto.setReducedMotion(settings.getReducedMotion());
        return dto;
    }
}
