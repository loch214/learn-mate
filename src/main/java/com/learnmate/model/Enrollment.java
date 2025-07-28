package com.learnmate.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attendance> attendanceRecords = new HashSet<>();

    // Constructors
    public Enrollment() {
    }

    public Enrollment(User student, Subject subject, LocalDate enrollmentDate) {
        this.student = student;
        this.subject = subject;
        this.enrollmentDate = enrollmentDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Set<Attendance> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(Set<Attendance> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

    // Helper methods
    public void addAttendanceRecord(Attendance attendance) {
        attendanceRecords.add(attendance);
        attendance.setEnrollment(this);
    }

    public void removeAttendanceRecord(Attendance attendance) {
        attendanceRecords.remove(attendance);
        attendance.setEnrollment(null);
    }
}