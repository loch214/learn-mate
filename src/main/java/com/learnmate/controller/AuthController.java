package com.learnmate.controller;

import com.learnmate.model.UserApplication;
import com.learnmate.repository.UserApplicationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserApplicationRepository applicationRepository, PasswordEncoder passwordEncoder) {
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(
            @RequestParam String fullName,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String role,
            @RequestParam String details,
            RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match!");
            return "redirect:/signup";
        }

        UserApplication application = new UserApplication();
        application.setFullName(fullName);
        application.setUsername(username);
        application.setEmail(email);
        application.setPassword(passwordEncoder.encode(password));
        application.setRole(role);
        application.setDetails(details);
        application.setStatus("PENDING");
        applicationRepository.save(application);
        redirectAttributes.addFlashAttribute("successMessage", "Your application has been submitted successfully! You will be notified upon approval.");
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        if (request.isUserInRole("ROLE_TEACHER")) {
            return "redirect:/teacher/dashboard";
        }
        if (request.isUserInRole("ROLE_STUDENT")) {
            return "redirect:/student/dashboard";
        }
        return "redirect:/login?error";
    }
}