package com.example.smarthire;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.UserRepository;
import com.example.smarthire.Security.JwtService;

@SpringBootTest
public class SecurityAndAuthTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testPasswordEncoderMatchesBCrypt() {
        String rawPassword = "SecurePassword@2026";
        String encoded = passwordEncoder.encode(rawPassword);

        assertNotNull(encoded);
        assertTrue(encoded.startsWith("$2a$") || encoded.startsWith("$2b$"));
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
        assertFalse(passwordEncoder.matches("WrongPassword", encoded));
    }

    @Test
    public void testJwtGenerationAndEmailExtraction() {
        String email = "testuser@smarthire.local";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                email,
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_JOB_SEEKER"))
        );

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        String extractedEmail = jwtService.extractUsername(token);
        assertEquals(email, extractedEmail);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testExistingUserAccountsPreserved() {
        long userCount = userRepository.count();
        assertTrue(userCount >= 5, "Existing users must be preserved in the database");

        boolean hasJobSeeker = userRepository.existsByRole("JOB_SEEKER");
        boolean hasRecruiter = userRepository.existsByRole("RECRUITER");
        boolean hasAdmin = userRepository.existsByRole("ADMIN");

        assertTrue(hasJobSeeker, "Must contain at least one JOB_SEEKER");
        assertTrue(hasRecruiter, "Must contain at least one RECRUITER");
        assertTrue(hasAdmin, "Must contain an ADMIN account");
    }
}
