package com.UNI.uni_sys.repository;

import com.UNI.uni_sys.model.Course;
import com.UNI.uni_sys.model.Faculty;
import com.UNI.uni_sys.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Find courses by faculty
    List<Course> findByFaculty(Faculty faculty);
    
    // Find courses by faculty ID
    List<Course> findByFacultyFacultyId(Long facultyId);
    
    // Find courses by professor
    List<Course> findByProfessor(Professor professor);
    
    // Find courses by professor ID
    List<Course> findByProfessorProfessorId(Long professorId);
    
    // Find courses by level
    List<Course> findByLevel(int level);
    
    // Find courses by name (case-insensitive)
    List<Course> findByCourseNameContainingIgnoreCase(String courseName);
    
    // Find course by name (exact match)
    Optional<Course> findByCourseName(String courseName);
    
    // Find courses by faculty and level
    List<Course> findByFacultyAndLevel(Faculty faculty, int level);
    
    // Find courses by professor and level
    List<Course> findByProfessorAndLevel(Professor professor, int level);
    
    // Custom query to find courses with their enrollments
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.enrollments WHERE c.courseId = :courseId")
    Optional<Course> findByIdWithEnrollments(@Param("courseId") Long courseId);
    
    // Custom query to find all courses with their enrollments
    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.enrollments")
    List<Course> findAllWithEnrollments();
    
    // Check if course exists by name
    boolean existsByCourseName(String courseName);
    
    // Count courses by faculty
    long countByFaculty(Faculty faculty);
    
    // Count courses by professor
    long countByProfessor(Professor professor);
    
    // Count courses by level
    long countByLevel(int level);

    // Delete by exact name; returns number of rows deleted
    long deleteByCourseName(String courseName);

    // Bulk delete by faculty ID
    long deleteByFacultyFacultyId(Long facultyId);

    // Bulk delete by professor ID
    long deleteByProfessorProfessorId(Long professorId);

    // Bulk delete by level
    long deleteByLevel(int level);
} 