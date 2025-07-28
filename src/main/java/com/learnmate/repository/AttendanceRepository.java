package com.learnmate.repository;

import com.learnmate.model.Attendance;
import com.learnmate.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    // Find all attendance records for a specific enrollment
    List<Attendance> findByEnrollment(Enrollment enrollment);
    
    // Find all attendance records for a specific enrollment between dates
    List<Attendance> findByEnrollmentAndDateBetween(Enrollment enrollment, LocalDate startDate, LocalDate endDate);
    
    // Find attendance record for a specific enrollment on a specific date
    Optional<Attendance> findByEnrollmentAndDate(Enrollment enrollment, LocalDate date);
    
    // Find all attendance records for a specific date
    List<Attendance> findByDate(LocalDate date);
    
    // Count present attendance records for a specific enrollment
    long countByEnrollmentAndPresentTrue(Enrollment enrollment);
    
    // Calculate attendance percentage for a specific enrollment
    @Query("SELECT (COUNT(CASE WHEN a.present = true THEN 1 ELSE null END) * 100.0 / COUNT(a)) FROM Attendance a WHERE a.enrollment = ?1")
    Double calculateAttendancePercentage(Enrollment enrollment);
}