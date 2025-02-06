package com.Project.demo.Service;

import com.Project.demo.Entity.LoginRequest;

import com.Project.demo.Entity.SignUpRequest;
import com.Project.demo.Entity.User;
import com.Project.demo.Repository.UserRepository;
// import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
// @RequiredArgsConstructor
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(SignUpRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());  // Correct call to getUsername()
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAgent(request.isAgent());
        userRepository.save(user);

        return "User registered successfully!";
    }

    public String authenticateUser(LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if (user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return user.get().isAgent() ? "Redirecting to agent dashboard..." : "Redirecting to normal user dashboard...";
        }
        throw new RuntimeException("Invalid credentials");
    }

}

