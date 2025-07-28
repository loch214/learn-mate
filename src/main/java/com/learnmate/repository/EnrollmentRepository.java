package com.learnmate.repository;

import com.learnmate.model.Enrollment;
import com.learnmate.model.Subject;
import com.learnmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // Find all enrollments for a specific subject
    List<Enrollment> findBySubject(Subject subject);
    
    // Find all enrollments for a specific student
    List<Enrollment> findByStudent(User student);
    
    // Find enrollment for a specific student in a specific subject
    Optional<Enrollment> findByStudentAndSubject(User student, Subject subject);
    
    // Find all enrollments for subjects taught by a specific teacher
    List<Enrollment> findBySubject_Teacher(User teacher);
    
    // Count enrollments for a specific subject
    long countBySubject(Subject subject);
}