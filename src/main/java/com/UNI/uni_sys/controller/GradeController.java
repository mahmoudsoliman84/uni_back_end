package com.UNI.uni_sys.controller;

import com.UNI.uni_sys.dto.GradeDTO;
import com.UNI.uni_sys.mapper.GradeMapper;
import com.UNI.uni_sys.model.Grade;
import com.UNI.uni_sys.model.Enrollment;
import com.UNI.uni_sys.repository.GradeRepository;
import com.UNI.uni_sys.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // GET all grades (DTO)
    @GetMapping
    public ResponseEntity<List<GradeDTO>> getAllGrades() {
        try {
            List<Grade> grades = gradeRepository.findAll();
            return ResponseEntity.ok(grades.stream().map(GradeMapper::toDTO).toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grade by ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<GradeDTO> getGradeById(@PathVariable Long id) {
        try {
            Optional<Grade> grade = gradeRepository.findById(id);
            return grade.map(g -> ResponseEntity.ok(GradeMapper.toDTO(g)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grade by ID with enrollment details
    @GetMapping("/{id}/with-details")
    public ResponseEntity<Grade> getGradeWithDetails(@PathVariable Long id) {
        try {
            Optional<Grade> grade = gradeRepository.findByIdWithEnrollmentDetails(id);
            return grade.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET all grades with enrollment details
    @GetMapping("/with-details")
    public ResponseEntity<List<Grade>> getAllGradesWithDetails() {
        try {
            List<Grade> grades = gradeRepository.findAllWithEnrollmentDetails();
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grades by student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable Long studentId) {
        try {
            List<Grade> grades = gradeRepository.findByStudentId(studentId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grades by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourse(@PathVariable Long courseId) {
        try {
            List<Grade> grades = gradeRepository.findByCourseId(courseId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grades by faculty ID
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Grade>> getGradesByFaculty(@PathVariable Long facultyId) {
        try {
            List<Grade> grades = gradeRepository.findByFacultyId(facultyId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grades by grade value (exact match)
    @GetMapping("/value/{grade}")
    public ResponseEntity<List<Grade>> getGradesByValue(@PathVariable Double grade) {
        try {
            List<Grade> grades = gradeRepository.findByGrade(grade);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grades greater than a value
    @GetMapping("/greater-than/{grade}")
    public ResponseEntity<List<Grade>> getGradesGreaterThan(@PathVariable Double grade) {
        try {
            List<Grade> grades = gradeRepository.findByGradeGreaterThan(grade);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grades less than a value
    @GetMapping("/less-than/{grade}")
    public ResponseEntity<List<Grade>> getGradesLessThan(@PathVariable Double grade) {
        try {
            List<Grade> grades = gradeRepository.findByGradeLessThan(grade);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET grades between two values
    @GetMapping("/range")
    public ResponseEntity<List<Grade>> getGradesInRange(
            @RequestParam Double minGrade, @RequestParam Double maxGrade) {
        try {
            List<Grade> grades = gradeRepository.findByGradeBetween(minGrade, maxGrade);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET average grade by course
    @GetMapping("/average/course/{courseId}")
    public ResponseEntity<Double> getAverageGradeByCourse(@PathVariable Long courseId) {
        try {
            Double average = gradeRepository.findAverageGradeByCourse(courseId);
            return ResponseEntity.ok(average != null ? average : 0.0);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET average grade by student
    @GetMapping("/average/student/{studentId}")
    public ResponseEntity<Double> getAverageGradeByStudent(@PathVariable Long studentId) {
        try {
            Double average = gradeRepository.findAverageGradeByStudent(studentId);
            return ResponseEntity.ok(average != null ? average : 0.0);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET average grade by faculty
    @GetMapping("/average/faculty/{facultyId}")
    public ResponseEntity<Double> getAverageGradeByFaculty(@PathVariable Long facultyId) {
        try {
            Double average = gradeRepository.findAverageGradeByFaculty(facultyId);
            return ResponseEntity.ok(average != null ? average : 0.0);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create new grade (DTO)
    @PostMapping
    public ResponseEntity<GradeDTO> createGrade(@RequestBody GradeDTO gradeDTO) {
        try {
            Grade grade = GradeMapper.toEntity(gradeDTO);

            // Validate enrollment exists
            if (gradeDTO.getEnrollmentId() != null) {
                Optional<Enrollment> enrollment = enrollmentRepository.findById(gradeDTO.getEnrollmentId());
                if (enrollment.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                grade.setEnrollment(enrollment.get());
            }

            // Check if grade already exists for this enrollment
            if (gradeRepository.existsByEnrollment(grade.getEnrollment())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Validate grade value (0.0 to 100.0)
            if (grade.getGrade() < 0.0 || grade.getGrade() > 100.0) {
                return ResponseEntity.badRequest().build();
            }

            Grade savedGrade = gradeRepository.save(grade);
            return ResponseEntity.status(HttpStatus.CREATED).body(GradeMapper.toDTO(savedGrade));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT update grade (DTO)
    @PutMapping("/{id}")
    public ResponseEntity<GradeDTO> updateGrade(@PathVariable Long id, @RequestBody GradeDTO gradeDTO) {
        try {
            Optional<Grade> existingGrade = gradeRepository.findById(id);
            if (existingGrade.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Grade currentGrade = existingGrade.get();
            
            // Update grade value
            if (gradeDTO.getGrade() != null) {
                // Validate grade value (0.0 to 100.0)
                if (gradeDTO.getGrade() < 0.0 || gradeDTO.getGrade() > 100.0) {
                    return ResponseEntity.badRequest().build();
                }
                currentGrade.setGrade(gradeDTO.getGrade());
            }

            // Update enrollment if provided
            if (gradeDTO.getEnrollmentId() != null) {
                Optional<Enrollment> enrollment = enrollmentRepository.findById(gradeDTO.getEnrollmentId());
                if (enrollment.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                currentGrade.setEnrollment(enrollment.get());
            }

            // Check if the new enrollment already has a grade (excluding current)
            if (gradeRepository.existsByEnrollment(currentGrade.getEnrollment()) &&
                !currentGrade.getGradeId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Grade updatedGrade = gradeRepository.save(currentGrade);
            return ResponseEntity.ok(GradeMapper.toDTO(updatedGrade));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE grade
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        try {
            if (!gradeRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            gradeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE grade by enrollment ID
    @DeleteMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<Void> deleteGradeByEnrollment(@PathVariable Long enrollmentId) {
        try {
            Optional<Enrollment> enrollment = enrollmentRepository.findById(enrollmentId);
            if (enrollment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            long deleted = gradeRepository.deleteByEnrollment(enrollment.get());
            if (deleted == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE grades by student
    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<Long> deleteGradesByStudent(@PathVariable Long studentId) {
        try {
            long deleted = gradeRepository.deleteByEnrollmentStudentStudentId(studentId);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE grades by course
    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<Long> deleteGradesByCourse(@PathVariable Long courseId) {
        try {
            long deleted = gradeRepository.deleteByEnrollmentCourseCourseId(courseId);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
