package com.learnmate.controller;

import com.learnmate.model.User;
import com.learnmate.model.UserApplication;
import com.learnmate.repository.UserApplicationRepository;
import com.learnmate.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserApplicationRepository applicationRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    // --- Application Management ---
    @GetMapping("/applications")
    public String viewApplications(Model model) {
        List<UserApplication> applications = applicationRepository.findAll();
        model.addAttribute("applications", applications);
        return "admin/applications";
    }

    @PostMapping("/applications/approve")
    public String approveApplication(@RequestParam Long id) {
        UserApplication app = applicationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid application Id:" + id));
        User newUser = new User();
        newUser.setFullName(app.getFullName());
        newUser.setUsername(app.getUsername());
        newUser.setEmail(app.getEmail());
        newUser.setPassword(app.getPassword());
        newUser.setRole("ROLE_" + app.getRole());
        newUser.setDetails(app.getDetails());
        userRepository.save(newUser);
        applicationRepository.delete(app);
        return "redirect:/admin/applications";
    }

    @PostMapping("/applications/reject")
    public String rejectApplication(@RequestParam Long id) {
        applicationRepository.deleteById(id);
        return "redirect:/admin/applications";
    }

    // --- Student Management ---
    @GetMapping("/students")
    public String listStudents(Model model) {
        List<User> students = userRepository.findByRole("ROLE_STUDENT");
        model.addAttribute("students", students);
        return "admin/students";
    }

    // --- Teacher Management ---
    @GetMapping("/teachers")
    public String listTeachers(Model model) {
        List<User> teachers = userRepository.findByRole("ROLE_TEACHER");
        model.addAttribute("teachers", teachers);
        return "admin/teachers";
    }
}