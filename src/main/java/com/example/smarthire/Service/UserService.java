package com.example.smarthire.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.smarthire.Dto.RegisterRequest;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
     UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getFullName(),
                request.getEmail(),
                encodedPassword,
                request.getPhone(),
                request.getRole()
        );

        return userRepository.save(user);
    }
}