package com.learnmate.repository;

import com.learnmate.model.Subject;
import com.learnmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    // Find all subjects taught by a specific teacher
    List<Subject> findByTeacher(User teacher);
    
    // Find a subject by name (case-insensitive)
    List<Subject> findByNameContainingIgnoreCase(String name);
    
    // Find subjects by name or description (case-insensitive)
    List<Subject> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}