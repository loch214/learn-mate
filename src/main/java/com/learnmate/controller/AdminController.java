package com.learnmate.controller;

import com.learnmate.model.Subject;
import com.learnmate.model.User;
import com.learnmate.model.UserApplication;
import com.learnmate.repository.SubjectRepository;
import com.learnmate.repository.UserApplicationRepository;
import com.learnmate.repository.UserRepository;
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
    private final SubjectRepository subjectRepository; // We still need this one

    // CORRECTED CONSTRUCTOR: PasswordEncoder has been removed.
    public AdminController(UserApplicationRepository applicationRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("currentPage", "dashboard");
        return "admin/dashboard";
    }

    // --- Application Management ---
    @GetMapping("/applications")
    public String viewApplications(Model model) {
        List<UserApplication> applications = applicationRepository.findAll();
        model.addAttribute("applications", applications);
        model.addAttribute("currentPage", "applications");
        return "admin/applications";
    }

    @PostMapping("/applications/approve")
    public String approveApplication(@RequestParam Long id) {
        UserApplication app = applicationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid application Id:" + id));
        User newUser = new User();
        newUser.setFullName(app.getFullName());
        newUser.setUsername(app.getUsername());
        newUser.setEmail(app.getEmail());
        // The password from the application is already safely hashed.
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

    // --- User Management ---
    @GetMapping("/students")
    public String listStudents(Model model) {
        List<User> students = userRepository.findByRole("ROLE_STUDENT");
        model.addAttribute("students", students);
        model.addAttribute("currentPage", "students");
        return "admin/students";
    }

    @GetMapping("/teachers")
    public String listTeachers(Model model) {
        List<User> teachers = userRepository.findByRole("ROLE_TEACHER");
        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", "teachers");
        return "admin/teachers";
    }

    // --- Subject Management ---
    @GetMapping("/subjects")
    public String listSubjects(Model model) {
        List<Subject> subjects = subjectRepository.findAll();
        List<User> teachers = userRepository.findByRole("ROLE_TEACHER");
        model.addAttribute("subjects", subjects);
        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", "subjects");
        return "admin/subjects";
    }

    @PostMapping("/subjects/add")
    public String addSubject(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam double monthlyFee,
                             @RequestParam(required = false) Long teacherId,
                             RedirectAttributes redirectAttributes) {
        Subject newSubject = new Subject();
        newSubject.setName(name);
        newSubject.setDescription(description);
        newSubject.setMonthlyFee(monthlyFee);
        if (teacherId != null) {
            User teacher = userRepository.findById(teacherId).orElse(null);
            newSubject.setTeacher(teacher);
        }
        subjectRepository.save(newSubject);
        redirectAttributes.addFlashAttribute("successMessage", "Subject created successfully.");
        return "redirect:/admin/subjects";
    }

    @PostMapping("/subjects/delete")
    public String deleteSubject(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        subjectRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Subject deleted successfully.");
        return "redirect:/admin/subjects";
    }
}