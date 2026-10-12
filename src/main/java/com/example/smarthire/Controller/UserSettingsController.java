package com.example.smarthire.Controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.smarthire.Dto.PasswordChangeRequest;
import com.example.smarthire.Dto.UserSettingsDto;
import com.example.smarthire.Service.UserSettingsService;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "http://localhost:5173")
public class UserSettingsController {

    @Autowired
    private UserSettingsService settingsService;

    @GetMapping
    public ResponseEntity<UserSettingsDto> getSettings(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(settingsService.getUserSettings(email));
    }

    @PutMapping
    public ResponseEntity<UserSettingsDto> updateSettings(
            @RequestBody UserSettingsDto dto,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(settingsService.updateUserSettings(email, dto));
    }

    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestBody PasswordChangeRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        settingsService.changePassword(email, request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}
