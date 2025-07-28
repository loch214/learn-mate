package com.learnmate.repository;

import com.learnmate.model.CourseMaterial;
import com.learnmate.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {
    
    // Find all course materials for a specific subject
    List<CourseMaterial> findBySubject(Subject subject);
    
    // Find all course materials for a specific subject and type
    List<CourseMaterial> findBySubjectAndType(Subject subject, String type);
    
    // Find course materials by title (case-insensitive)
    List<CourseMaterial> findByTitleContainingIgnoreCase(String title);
    
    // Find course materials by title or description (case-insensitive)
    List<CourseMaterial> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    
    // Find course materials for a specific subject ordered by creation date (newest first)
    List<CourseMaterial> findBySubjectOrderByCreatedAtDesc(Subject subject);
}