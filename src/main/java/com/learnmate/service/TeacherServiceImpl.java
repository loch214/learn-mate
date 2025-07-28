package com.learnmate.service;

import com.learnmate.model.*;
import com.learnmate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final CourseMaterialRepository courseMaterialRepository;

    @Autowired
    public TeacherServiceImpl(SubjectRepository subjectRepository,
                             EnrollmentRepository enrollmentRepository,
                             AttendanceRepository attendanceRepository,
                             CourseMaterialRepository courseMaterialRepository) {
        this.subjectRepository = subjectRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.attendanceRepository = attendanceRepository;
        this.courseMaterialRepository = courseMaterialRepository;
    }

    // Subject related methods
    @Override
    public List<Subject> getSubjectsByTeacher(User teacher) {
        return subjectRepository.findByTeacher(teacher);
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with id: " + id));
    }

    // Student related methods
    @Override
    public List<User> getStudentsBySubject(Subject subject) {
        return enrollmentRepository.findBySubject(subject)
                .stream()
                .map(Enrollment::getStudent)
                .collect(Collectors.toList());
    }

    @Override
    public Map<User, Double> getStudentsWithAttendancePercentage(Subject subject) {
        List<Enrollment> enrollments = enrollmentRepository.findBySubject(subject);
        Map<User, Double> result = new HashMap<>();
        
        for (Enrollment enrollment : enrollments) {
            Double percentage = attendanceRepository.calculateAttendancePercentage(enrollment);
            result.put(enrollment.getStudent(), percentage != null ? percentage : 0.0);
        }
        
        return result;
    }

    // Enrollment related methods
    @Override
    public List<Enrollment> getEnrollmentsBySubject(Subject subject) {
        return enrollmentRepository.findBySubject(subject);
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found with id: " + id));
    }

    // Attendance related methods
    @Override
    public List<Attendance> getAttendanceByEnrollment(Enrollment enrollment) {
        return attendanceRepository.findByEnrollment(enrollment);
    }

    @Override
    public List<Attendance> getAttendanceByEnrollmentBetweenDates(Enrollment enrollment, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByEnrollmentAndDateBetween(enrollment, startDate, endDate);
    }

    @Override
    public Attendance getAttendanceByEnrollmentAndDate(Enrollment enrollment, LocalDate date) {
        return attendanceRepository.findByEnrollmentAndDate(enrollment, date)
                .orElse(null);
    }

    @Override
    @Transactional
    public Attendance markAttendance(Enrollment enrollment, LocalDate date, boolean present, String remarks) {
        // Check if attendance record already exists
        Attendance attendance = attendanceRepository.findByEnrollmentAndDate(enrollment, date)
                .orElse(new Attendance(enrollment, date, present));
        
        // Update attendance
        attendance.setPresent(present);
        attendance.setRemarks(remarks);
        
        return attendanceRepository.save(attendance);
    }

    @Override
    public Double calculateAttendancePercentage(Enrollment enrollment) {
        return attendanceRepository.calculateAttendancePercentage(enrollment);
    }

    // Course Material related methods
    @Override
    public List<CourseMaterial> getCourseMaterialsBySubject(Subject subject) {
        return courseMaterialRepository.findBySubjectOrderByCreatedAtDesc(subject);
    }

    @Override
    public CourseMaterial getCourseMaterialById(Long id) {
        return courseMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course material not found with id: " + id));
    }

    @Override
    @Transactional
    public CourseMaterial createCourseMaterial(Subject subject, String title, String description, String type, 
                                              String fileUrl, String linkUrl, String content) {
        CourseMaterial material = new CourseMaterial(subject, title, description, type);
        material.setFileUrl(fileUrl);
        material.setLinkUrl(linkUrl);
        material.setContent(content);
        
        return courseMaterialRepository.save(material);
    }

    @Override
    @Transactional
    public CourseMaterial updateCourseMaterial(Long id, String title, String description, String type, 
                                              String fileUrl, String linkUrl, String content) {
        CourseMaterial material = getCourseMaterialById(id);
        
        material.setTitle(title);
        material.setDescription(description);
        material.setType(type);
        material.setFileUrl(fileUrl);
        material.setLinkUrl(linkUrl);
        material.setContent(content);
        material.setUpdatedAt(LocalDateTime.now());
        
        return courseMaterialRepository.save(material);
    }

    @Override
    @Transactional
    public void deleteCourseMaterial(Long id) {
        courseMaterialRepository.deleteById(id);
    }
}