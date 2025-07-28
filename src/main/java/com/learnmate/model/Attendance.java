package com.learnmate.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean present;

    @Column
    private String remarks;

    // Constructors
    public Attendance() {
    }

    public Attendance(Enrollment enrollment, LocalDate date, boolean present) {
        this.enrollment = enrollment;
        this.date = date;
        this.present = present;
    }

    public Attendance(Enrollment enrollment, LocalDate date, boolean present, String remarks) {
        this.enrollment = enrollment;
        this.date = date;
        this.present = present;
        this.remarks = remarks;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}