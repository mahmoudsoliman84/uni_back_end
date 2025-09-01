package com.UNI.uni_sys.repository;

import com.UNI.uni_sys.model.Student;
import com.UNI.uni_sys.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find students by faculty
    List<Student> findByFaculty(Faculty faculty);
    
    // Find students by faculty ID
    List<Student> findByFacultyFacultyId(Long facultyId);
    
    // Find students by level
    List<Student> findByLevel(int level);
    
    // Find students by name (case-insensitive)
    List<Student> findByStudentNameContainingIgnoreCase(String studentName);
    
    // Find student by name (exact match)
    Optional<Student> findByStudentName(String studentName);
    
    // Find students by faculty and level
    List<Student> findByFacultyAndLevel(Faculty faculty, int level);
    
    // Custom query to find students with their enrollments
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.enrollments WHERE s.studentId = :studentId")
    Optional<Student> findByIdWithEnrollments(@Param("studentId") Long studentId);
    
    // Custom query to find all students with their enrollments
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.enrollments")
    List<Student> findAllWithEnrollments();
    
    // Check if student exists by name
    boolean existsByStudentName(String studentName);
    
    // Count students by faculty
    long countByFaculty(Faculty faculty);
    
    // Count students by level
    long countByLevel(int level);

    // Delete by exact name; returns number of rows deleted
    long deleteByStudentName(String studentName);

    // Bulk delete by faculty ID; returns number of rows deleted
    long deleteByFacultyFacultyId(Long facultyId);

    // Bulk delete by level
    long deleteByLevel(int level);

    // Bulk delete by faculty and level
    long deleteByFacultyFacultyIdAndLevel(Long facultyId, int level);
} 