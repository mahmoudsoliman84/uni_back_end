package com.UNI.uni_sys.controller;

import com.UNI.uni_sys.dto.EnrollmentDTO;
import com.UNI.uni_sys.mapper.EnrollmentMapper;
import com.UNI.uni_sys.model.Enrollment;
import com.UNI.uni_sys.model.Student;
import com.UNI.uni_sys.model.Course;
import com.UNI.uni_sys.repository.EnrollmentRepository;
import com.UNI.uni_sys.repository.StudentRepository;
import com.UNI.uni_sys.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // GET all enrollments (DTO)
    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        try {
            List<Enrollment> enrollments = enrollmentRepository.findAll();
            return ResponseEntity.ok(enrollments.stream().map(EnrollmentMapper::toDTO).toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET enrollment by ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
        try {
            Optional<Enrollment> enrollment = enrollmentRepository.findById(id);
            return enrollment.map(e -> ResponseEntity.ok(EnrollmentMapper.toDTO(e)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET enrollment by ID with grade
    @GetMapping("/{id}/with-grade")
    public ResponseEntity<Enrollment> getEnrollmentWithGrade(@PathVariable Long id) {
        try {
            Optional<Enrollment> enrollment = enrollmentRepository.findByIdWithGrade(id);
            return enrollment.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET all enrollments with grades
    @GetMapping("/with-grades")
    public ResponseEntity<List<Enrollment>> getAllEnrollmentsWithGrades() {
        try {
            List<Enrollment> enrollments = enrollmentRepository.findAllWithGrades();
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET enrollments by student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        try {
            Optional<Student> student = studentRepository.findById(studentId);
            if (student.isPresent()) {
                List<Enrollment> enrollments = enrollmentRepository.findByStudent(student.get());
                return ResponseEntity.ok(enrollments);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET enrollments by student with grades
    @GetMapping("/student/{studentId}/with-grades")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentWithGrades(@PathVariable Long studentId) {
        try {
            List<Enrollment> enrollments = enrollmentRepository.findByStudentIdWithGrade(studentId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET enrollments by course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        try {
            Optional<Course> course = courseRepository.findById(courseId);
            if (course.isPresent()) {
                List<Enrollment> enrollments = enrollmentRepository.findByCourse(course.get());
                return ResponseEntity.ok(enrollments);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET enrollments by course with grades
    @GetMapping("/course/{courseId}/with-grades")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseWithGrades(@PathVariable Long courseId) {
        try {
            List<Enrollment> enrollments = enrollmentRepository.findByCourseIdWithGrade(courseId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET enrollments by student level
    @GetMapping("/student-level/{level}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentLevel(@PathVariable int level) {
        try {
            List<Enrollment> enrollments = enrollmentRepository.findByStudentLevel(level);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET enrollment by student and course
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Enrollment> getEnrollmentByStudentAndCourse(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        try {
            Optional<Student> student = studentRepository.findById(studentId);
            Optional<Course> course = courseRepository.findById(courseId);
            
            if (student.isPresent() && course.isPresent()) {
                Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndCourse(student.get(), course.get());
                return enrollment.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create new enrollment (DTO)
    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        try {
            Enrollment enrollment = EnrollmentMapper.toEntity(enrollmentDTO);

            // Validate student exists
            if (enrollmentDTO.getStudentId() != null) {
                Optional<Student> student = studentRepository.findById(enrollmentDTO.getStudentId());
                if (student.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                enrollment.setStudent(student.get());
            }

            // Validate course exists
            if (enrollmentDTO.getCourseId() != null) {
                Optional<Course> course = courseRepository.findById(enrollmentDTO.getCourseId());
                if (course.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                enrollment.setCourse(course.get());
            }

            // Check if enrollment already exists
            if (enrollmentRepository.existsByStudentAndCourse(enrollment.getStudent(), enrollment.getCourse())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(EnrollmentMapper.toDTO(savedEnrollment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT update enrollment (DTO)
    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(@PathVariable Long id, @RequestBody EnrollmentDTO enrollmentDTO) {
        try {
            Optional<Enrollment> existingEnrollment = enrollmentRepository.findById(id);
            if (existingEnrollment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Enrollment currentEnrollment = existingEnrollment.get();
            
            // Update student if provided
            if (enrollmentDTO.getStudentId() != null) {
                Optional<Student> student = studentRepository.findById(enrollmentDTO.getStudentId());
                if (student.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                currentEnrollment.setStudent(student.get());
            }

            // Update course if provided
            if (enrollmentDTO.getCourseId() != null) {
                Optional<Course> course = courseRepository.findById(enrollmentDTO.getCourseId());
                if (course.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                currentEnrollment.setCourse(course.get());
            }

            // Check if the new combination already exists (excluding current)
            if (enrollmentRepository.existsByStudentAndCourse(currentEnrollment.getStudent(), currentEnrollment.getCourse()) &&
                !currentEnrollment.getEnrollmentId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Enrollment updatedEnrollment = enrollmentRepository.save(currentEnrollment);
            return ResponseEntity.ok(EnrollmentMapper.toDTO(updatedEnrollment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE enrollment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        try {
            if (!enrollmentRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            enrollmentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE enrollment by student and course
    @DeleteMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Void> deleteEnrollmentByStudentAndCourse(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        try {
            Optional<Student> student = studentRepository.findById(studentId);
            Optional<Course> course = courseRepository.findById(courseId);
            if (student.isEmpty() || course.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            long deleted = enrollmentRepository.deleteByStudentAndCourse(student.get(), course.get());
            if (deleted == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE enrollments by student
    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<Long> deleteEnrollmentsByStudent(@PathVariable Long studentId) {
        try {
            long deleted = enrollmentRepository.deleteByStudentStudentId(studentId);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE enrollments by course
    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<Long> deleteEnrollmentsByCourse(@PathVariable Long courseId) {
        try {
            long deleted = enrollmentRepository.deleteByCourseCourseId(courseId);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET count of enrollments by student
    @GetMapping("/count/student/{studentId}")
    public ResponseEntity<Long> getEnrollmentCountByStudent(@PathVariable Long studentId) {
        try {
            Optional<Student> student = studentRepository.findById(studentId);
            if (student.isPresent()) {
                long count = enrollmentRepository.countByStudent(student.get());
                return ResponseEntity.ok(count);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET count of enrollments by course
    @GetMapping("/count/course/{courseId}")
    public ResponseEntity<Long> getEnrollmentCountByCourse(@PathVariable Long courseId) {
        try {
            Optional<Course> course = courseRepository.findById(courseId);
            if (course.isPresent()) {
                long count = enrollmentRepository.countByCourse(course.get());
                return ResponseEntity.ok(count);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
