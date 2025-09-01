package com.UNI.uni_sys.repository;

import com.UNI.uni_sys.model.Professor;
import com.UNI.uni_sys.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    // Find professors by faculty
    List<Professor> findByFaculty(Faculty faculty);
    
    // Find professors by faculty ID
    List<Professor> findByFacultyFacultyId(Long facultyId);
    
    // Find professor by name (case-insensitive)
    List<Professor> findByProfessorNameContainingIgnoreCase(String professorName);
    
    // Find professor by name (exact match)
    Optional<Professor> findByProfessorName(String professorName);
    
    // Find professors by faculty and name
    List<Professor> findByFacultyAndProfessorNameContainingIgnoreCase(Faculty faculty, String professorName);
    
    // Custom query to find professor with courses
    @Query("SELECT p FROM Professor p LEFT JOIN FETCH p.courses WHERE p.professorId = :professorId")
    Optional<Professor> findByIdWithCourses(@Param("professorId") Long professorId);
    
    // Custom query to find all professors with their courses
    @Query("SELECT DISTINCT p FROM Professor p LEFT JOIN FETCH p.courses")
    List<Professor> findAllWithCourses();
    
    // Check if professor exists by name
    boolean existsByProfessorName(String professorName);
    
    // Count professors by faculty
    long countByFaculty(Faculty faculty);
    
    // Find professors who teach courses at a specific level
    @Query("SELECT DISTINCT p FROM Professor p JOIN p.courses c WHERE c.level = :level")
    List<Professor> findByTeachingLevel(@Param("level") int level);

    // Delete professor by exact name; returns number of rows deleted
    long deleteByProfessorName(String professorName);

    // Delete all professors by faculty ID; returns number of rows deleted
    long deleteByFacultyFacultyId(Long facultyId);
} 