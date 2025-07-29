package com.learnmate.controller;

import com.learnmate.model.*;
import com.learnmate.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final com.learnmate.repository.UserRepository userRepository;
    
    @Autowired
    public TeacherController(TeacherService teacherService, com.learnmate.repository.UserRepository userRepository) {
        this.teacherService = teacherService;
        this.userRepository = userRepository;
    }
    
    // Helper method to get the currently logged-in user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String teacherDashboard(Model model) {
        User teacher = getCurrentUser();
        List<Subject> subjects = teacherService.getSubjectsByTeacher(teacher);
        
        model.addAttribute("teacher", teacher);
        model.addAttribute("subjects", subjects);
        model.addAttribute("currentPage", "dashboard");
        
        return "teacher/dashboard";
    }
    
    // Subjects/Classes
    @GetMapping("/subjects")
    public String listSubjects(Model model) {
        User teacher = getCurrentUser();
        List<Subject> subjects = teacherService.getSubjectsByTeacher(teacher);
        
        model.addAttribute("subjects", subjects);
        model.addAttribute("currentPage", "subjects");
        
        return "teacher/subjects";
    }
    
    @GetMapping("/subjects/{id}")
    public String viewSubject(@PathVariable Long id, Model model) {
        Subject subject = teacherService.getSubjectById(id);
        List<User> students = teacherService.getStudentsBySubject(subject);
        
        model.addAttribute("subject", subject);
        model.addAttribute("students", students);
        
        return "teacher/subject-details";
    }
    
    // Students
    @GetMapping("/subjects/{id}/students")
    public String listStudents(@PathVariable Long id, Model model) {
        Subject subject = teacherService.getSubjectById(id);
        Map<User, Double> studentsWithAttendance = teacherService.getStudentsWithAttendancePercentage(subject);
        
        model.addAttribute("subject", subject);
        model.addAttribute("studentsWithAttendance", studentsWithAttendance);
        
        return "teacher/students";
    }
    
    // Attendance
    @GetMapping("/subjects/{id}/attendance")
    public String manageAttendance(@PathVariable Long id, Model model) {
        Subject subject = teacherService.getSubjectById(id);
        List<Enrollment> enrollments = teacherService.getEnrollmentsBySubject(subject);
        
        model.addAttribute("subject", subject);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("today", LocalDate.now());
        
        return "teacher/attendance";
    }
    
    @PostMapping("/subjects/{subjectId}/attendance")
    public String markAttendance(@PathVariable Long subjectId,
                                @RequestParam Long enrollmentId,
                                @RequestParam LocalDate date,
                                @RequestParam boolean present,
                                @RequestParam(required = false) String remarks,
                                RedirectAttributes redirectAttributes) {
        
        Enrollment enrollment = teacherService.getEnrollmentById(enrollmentId);
        teacherService.markAttendance(enrollment, date, present, remarks);
        
        redirectAttributes.addFlashAttribute("success", "Attendance marked successfully");
        return "redirect:/teacher/subjects/" + subjectId + "/attendance";
    }
    
    // Course Materials
    @GetMapping("/subjects/{id}/materials")
    public String listMaterials(@PathVariable Long id, Model model) {
        Subject subject = teacherService.getSubjectById(id);
        List<CourseMaterial> materials = teacherService.getCourseMaterialsBySubject(subject);
        
        model.addAttribute("subject", subject);
        model.addAttribute("materials", materials);
        
        return "teacher/materials";
    }
    
    @GetMapping("/subjects/{id}/materials/add")
    public String showAddMaterialForm(@PathVariable Long id, Model model) {
        Subject subject = teacherService.getSubjectById(id);
        
        model.addAttribute("subject", subject);
        model.addAttribute("material", new CourseMaterial());
        
        return "teacher/add-material";
    }
    
    @PostMapping("/subjects/{id}/materials")
    public String addMaterial(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String type,
                             @RequestParam(required = false) String fileUrl,
                             @RequestParam(required = false) String linkUrl,
                             @RequestParam(required = false) String content,
                             RedirectAttributes redirectAttributes) {
        
        Subject subject = teacherService.getSubjectById(id);
        teacherService.createCourseMaterial(subject, title, description, type, fileUrl, linkUrl, content);
        
        redirectAttributes.addFlashAttribute("success", "Course material added successfully");
        return "redirect:/teacher/subjects/" + id + "/materials";
    }
    
    @GetMapping("/materials/{id}/edit")
    public String showEditMaterialForm(@PathVariable Long id, Model model) {
        CourseMaterial material = teacherService.getCourseMaterialById(id);
        
        model.addAttribute("material", material);
        
        return "teacher/edit-material";
    }
    
    @PostMapping("/materials/{id}")
    public String updateMaterial(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String description,
                                @RequestParam String type,
                                @RequestParam(required = false) String fileUrl,
                                @RequestParam(required = false) String linkUrl,
                                @RequestParam(required = false) String content,
                                RedirectAttributes redirectAttributes) {
        
        CourseMaterial material = teacherService.updateCourseMaterial(id, title, description, type, fileUrl, linkUrl, content);
        
        redirectAttributes.addFlashAttribute("success", "Course material updated successfully");
        return "redirect:/teacher/subjects/" + material.getSubject().getId() + "/materials";
    }
    
    @PostMapping("/materials/{id}/delete")
    public String deleteMaterial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        CourseMaterial material = teacherService.getCourseMaterialById(id);
        Long subjectId = material.getSubject().getId();
        
        teacherService.deleteCourseMaterial(id);
        
        redirectAttributes.addFlashAttribute("success", "Course material deleted successfully");
        return "redirect:/teacher/subjects/" + subjectId + "/materials";
    }
}