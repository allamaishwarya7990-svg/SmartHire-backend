package com.example.smarthire.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.smarthire.Dto.LoginRequest;
import com.example.smarthire.Dto.LoginResponse;
import com.example.smarthire.Dto.RegisterRequest;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Security.JwtService;
import com.example.smarthire.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public User registerUser(
            @RequestBody RegisterRequest request) {

        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse loginUser(
            @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token =
                jwtService.generateToken(userDetails);

        String role =
                userDetails.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace("ROLE_", "");

        return new LoginResponse(
                token,
                userDetails.getUsername(),
                role
        );
    }
}