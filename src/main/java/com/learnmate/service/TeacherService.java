package com.learnmate.service;

import com.learnmate.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TeacherService {
    
    // Subject related methods
    List<Subject> getSubjectsByTeacher(User teacher);
    Subject getSubjectById(Long id);
    
    // Student related methods
    List<User> getStudentsBySubject(Subject subject);
    Map<User, Double> getStudentsWithAttendancePercentage(Subject subject);
    
    // Enrollment related methods
    List<Enrollment> getEnrollmentsBySubject(Subject subject);
    Enrollment getEnrollmentById(Long id);
    
    // Attendance related methods
    List<Attendance> getAttendanceByEnrollment(Enrollment enrollment);
    List<Attendance> getAttendanceByEnrollmentBetweenDates(Enrollment enrollment, LocalDate startDate, LocalDate endDate);
    Attendance getAttendanceByEnrollmentAndDate(Enrollment enrollment, LocalDate date);
    Attendance markAttendance(Enrollment enrollment, LocalDate date, boolean present, String remarks);
    Double calculateAttendancePercentage(Enrollment enrollment);
    
    // Course Material related methods
    List<CourseMaterial> getCourseMaterialsBySubject(Subject subject);
    CourseMaterial getCourseMaterialById(Long id);
    CourseMaterial createCourseMaterial(Subject subject, String title, String description, String type, String fileUrl, String linkUrl, String content);
    CourseMaterial updateCourseMaterial(Long id, String title, String description, String type, String fileUrl, String linkUrl, String content);
    void deleteCourseMaterial(Long id);
}