package com.UNI.uni_sys.repository;

import com.UNI.uni_sys.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    
    // Find faculty by name (case-insensitive)
    List<Faculty> findByFacultyNameContainingIgnoreCase(String facultyName);
    
    // Find faculty by name (exact match)
    Optional<Faculty> findByFacultyName(String facultyName);
    
    // Check if faculty exists by name
    boolean existsByFacultyName(String facultyName);
    
    // Custom query to find faculty with students
    @Query("SELECT f FROM Faculty f LEFT JOIN FETCH f.students WHERE f.facultyId = :facultyId")
    Optional<Faculty> findByIdWithStudents(@Param("facultyId") Long facultyId);
    
    // Custom query to find faculty with professors
    @Query("SELECT f FROM Faculty f LEFT JOIN FETCH f.professors WHERE f.facultyId = :facultyId")
    Optional<Faculty> findByIdWithProfessors(@Param("facultyId") Long facultyId);
    
    // Custom query to find faculty with courses
    @Query("SELECT f FROM Faculty f LEFT JOIN FETCH f.courses WHERE f.facultyId = :facultyId")
    Optional<Faculty> findByIdWithCourses(@Param("facultyId") Long facultyId);
    
    // Custom query to find faculty with all related entities
    @Query("SELECT f FROM Faculty f LEFT JOIN FETCH f.students LEFT JOIN FETCH f.professors LEFT JOIN FETCH f.courses WHERE f.facultyId = :facultyId")
    Optional<Faculty> findByIdWithAllDetails(@Param("facultyId") Long facultyId);
    
    // Custom query to find all faculty with their students
    @Query("SELECT DISTINCT f FROM Faculty f LEFT JOIN FETCH f.students")
    List<Faculty> findAllWithStudents();
    
    // Custom query to find all faculty with their professors
    @Query("SELECT DISTINCT f FROM Faculty f LEFT JOIN FETCH f.professors")
    List<Faculty> findAllWithProfessors();
    
    // Custom query to find all faculty with their courses
    @Query("SELECT DISTINCT f FROM Faculty f LEFT JOIN FETCH f.courses")
    List<Faculty> findAllWithCourses();

    // Delete by exact name; returns number of rows deleted
    long deleteByFacultyName(String facultyName);
} 