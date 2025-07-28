package com.learnmate.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseMaterial> courseMaterials = new HashSet<>();

    // Constructors
    public Subject() {
    }

    public Subject(String name, String description, User teacher) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public Set<CourseMaterial> getCourseMaterials() {
        return courseMaterials;
    }

    public void setCourseMaterials(Set<CourseMaterial> courseMaterials) {
        this.courseMaterials = courseMaterials;
    }

    // Helper methods
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setSubject(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setSubject(null);
    }

    public void addCourseMaterial(CourseMaterial material) {
        courseMaterials.add(material);
        material.setSubject(this);
    }

    public void removeCourseMaterial(CourseMaterial material) {
        courseMaterials.remove(material);
        material.setSubject(null);
    }
}