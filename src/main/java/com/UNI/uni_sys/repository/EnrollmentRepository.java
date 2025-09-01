package com.UNI.uni_sys.repository;

import com.UNI.uni_sys.model.Enrollment;
import com.UNI.uni_sys.model.Student;
import com.UNI.uni_sys.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // Find enrollments by student
    List<Enrollment> findByStudent(Student student);
    
    // Find enrollments by student ID
    List<Enrollment> findByStudentStudentId(Long studentId);
    
    // Find enrollments by course
    List<Enrollment> findByCourse(Course course);
    
    // Find enrollments by course ID
    List<Enrollment> findByCourseCourseId(Long courseId);
    
    // Find enrollment by student and course
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
    
    // Find enrollment by student ID and course ID
    Optional<Enrollment> findByStudentStudentIdAndCourseCourseId(Long studentId, Long courseId);
    
    // Check if enrollment exists by student and course
    boolean existsByStudentAndCourse(Student student, Course course);
    
    // Check if enrollment exists by student ID and course ID
    boolean existsByStudentStudentIdAndCourseCourseId(Long studentId, Long courseId);
    
    // Count enrollments by student
    long countByStudent(Student student);
    
    // Count enrollments by course
    long countByCourse(Course course);
    
    // Custom query to find enrollments with grades
    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.grade WHERE e.enrollmentId = :enrollmentId")
    Optional<Enrollment> findByIdWithGrade(@Param("enrollmentId") Long enrollmentId);
    
    // Custom query to find all enrollments with grades
    @Query("SELECT DISTINCT e FROM Enrollment e LEFT JOIN FETCH e.grade")
    List<Enrollment> findAllWithGrades();
    
    // Custom query to find enrollments by student with grades
    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.grade WHERE e.student.studentId = :studentId")
    List<Enrollment> findByStudentIdWithGrade(@Param("studentId") Long studentId);
    
    // Custom query to find enrollments by course with grades
    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.grade WHERE e.course.courseId = :courseId")
    List<Enrollment> findByCourseIdWithGrade(@Param("courseId") Long courseId);
    
    // Find enrollments for a specific academic level
    @Query("SELECT e FROM Enrollment e WHERE e.student.level = :level")
    List<Enrollment> findByStudentLevel(@Param("level") int level);

    // Delete by student and course; returns number of rows deleted
    long deleteByStudentAndCourse(Student student, Course course);

    // Bulk delete by student ID
    long deleteByStudentStudentId(Long studentId);

    // Bulk delete by course ID
    long deleteByCourseCourseId(Long courseId);
} 