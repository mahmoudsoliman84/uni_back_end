package com.UNI.uni_sys.repository;

import com.UNI.uni_sys.model.Grade;
import com.UNI.uni_sys.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    // Find grade by enrollment
    Optional<Grade> findByEnrollment(Enrollment enrollment);
    
    // Find grade by enrollment ID
    Optional<Grade> findByEnrollmentEnrollmentId(Long enrollmentId);
    
    // Find grades by grade value (exact match)
    List<Grade> findByGrade(Double grade);
    
    // Find grades greater than a specific value
    List<Grade> findByGradeGreaterThan(Double grade);
    
    // Find grades less than a specific value
    List<Grade> findByGradeLessThan(Double grade);
    
    // Find grades between two values
    List<Grade> findByGradeBetween(Double minGrade, Double maxGrade);
    
    // Find grades greater than or equal to a specific value
    List<Grade> findByGradeGreaterThanEqual(Double grade);
    
    // Find grades less than or equal to a specific value
    List<Grade> findByGradeLessThanEqual(Double grade);
    
    // Check if grade exists by enrollment
    boolean existsByEnrollment(Enrollment enrollment);
    
    // Check if grade exists by enrollment ID
    boolean existsByEnrollmentEnrollmentId(Long enrollmentId);
    
    // Custom query to find grades with enrollment details
    @Query("SELECT g FROM Grade g LEFT JOIN FETCH g.enrollment e LEFT JOIN FETCH e.student LEFT JOIN FETCH e.course WHERE g.gradeId = :gradeId")
    Optional<Grade> findByIdWithEnrollmentDetails(@Param("gradeId") Long gradeId);
    
    // Custom query to find all grades with enrollment details
    @Query("SELECT DISTINCT g FROM Grade g LEFT JOIN FETCH g.enrollment e LEFT JOIN FETCH e.student LEFT JOIN FETCH e.course")
    List<Grade> findAllWithEnrollmentDetails();
    
    // Custom query to find grades by student ID
    @Query("SELECT g FROM Grade g JOIN g.enrollment e WHERE e.student.studentId = :studentId")
    List<Grade> findByStudentId(@Param("studentId") Long studentId);
    
    // Custom query to find grades by course ID
    @Query("SELECT g FROM Grade g JOIN g.enrollment e WHERE e.course.courseId = :courseId")
    List<Grade> findByCourseId(@Param("courseId") Long courseId);
    
    // Custom query to find grades by faculty ID
    @Query("SELECT g FROM Grade g JOIN g.enrollment e JOIN e.course c WHERE c.faculty.facultyId = :facultyId")
    List<Grade> findByFacultyId(@Param("facultyId") Long facultyId);
    
    // Custom query to find average grade by course
    @Query("SELECT AVG(g.grade) FROM Grade g JOIN g.enrollment e WHERE e.course.courseId = :courseId")
    Double findAverageGradeByCourse(@Param("courseId") Long courseId);
    
    // Custom query to find average grade by student
    @Query("SELECT AVG(g.grade) FROM Grade g JOIN g.enrollment e WHERE e.student.studentId = :studentId")
    Double findAverageGradeByStudent(@Param("studentId") Long studentId);
    
    // Custom query to find average grade by faculty
    @Query("SELECT AVG(g.grade) FROM Grade g JOIN g.enrollment e JOIN e.course c WHERE c.faculty.facultyId = :facultyId")
    Double findAverageGradeByFaculty(@Param("facultyId") Long facultyId);

    // Delete grade by enrollment; returns number of rows deleted
    long deleteByEnrollment(Enrollment enrollment);

    // Bulk delete grades by student ID
    long deleteByEnrollmentStudentStudentId(Long studentId);
    
    // Bulk delete grades by course ID
    long deleteByEnrollmentCourseCourseId(Long courseId);
} 