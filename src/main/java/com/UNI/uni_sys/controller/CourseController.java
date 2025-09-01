package com.UNI.uni_sys.controller;

import com.UNI.uni_sys.dto.CourseDTO;
import com.UNI.uni_sys.mapper.CourseMapper;
import com.UNI.uni_sys.model.Course;
import com.UNI.uni_sys.model.Faculty;
import com.UNI.uni_sys.model.Professor;
import com.UNI.uni_sys.repository.CourseRepository;
import com.UNI.uni_sys.repository.FacultyRepository;
import com.UNI.uni_sys.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    // GET all courses (DTO)
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        try {
            List<Course> courses = courseRepository.findAll();
            return ResponseEntity.ok(courses.stream().map(CourseMapper::toDTO).toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET course by ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        try {
            Optional<Course> course = courseRepository.findById(id);
            return course.map(c -> ResponseEntity.ok(CourseMapper.toDTO(c)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET course by ID with enrollments
    @GetMapping("/{id}/with-enrollments")
    public ResponseEntity<Course> getCourseWithEnrollments(@PathVariable Long id) {
        try {
            Optional<Course> course = courseRepository.findByIdWithEnrollments(id);
            return course.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET courses by faculty
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Course>> getCoursesByFaculty(@PathVariable Long facultyId) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                List<Course> courses = courseRepository.findByFaculty(faculty.get());
                return ResponseEntity.ok(courses);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET courses by professor
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Course>> getCoursesByProfessor(@PathVariable Long professorId) {
        try {
            Optional<Professor> professor = professorRepository.findById(professorId);
            if (professor.isPresent()) {
                List<Course> courses = courseRepository.findByProfessor(professor.get());
                return ResponseEntity.ok(courses);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET courses by level
    @GetMapping("/level/{level}")
    public ResponseEntity<List<Course>> getCoursesByLevel(@PathVariable int level) {
        try {
            List<Course> courses = courseRepository.findByLevel(level);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET courses by faculty and level
    @GetMapping("/faculty/{facultyId}/level/{level}")
    public ResponseEntity<List<Course>> getCoursesByFacultyAndLevel(
            @PathVariable Long facultyId, @PathVariable int level) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                List<Course> courses = courseRepository.findByFacultyAndLevel(faculty.get(), level);
                return ResponseEntity.ok(courses);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET courses by professor and level
    @GetMapping("/professor/{professorId}/level/{level}")
    public ResponseEntity<List<Course>> getCoursesByProfessorAndLevel(
            @PathVariable Long professorId, @PathVariable int level) {
        try {
            Optional<Professor> professor = professorRepository.findById(professorId);
            if (professor.isPresent()) {
                List<Course> courses = courseRepository.findByProfessorAndLevel(professor.get(), level);
                return ResponseEntity.ok(courses);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET courses by name (search)
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCoursesByName(@RequestParam String name) {
        try {
            List<Course> courses = courseRepository.findByCourseNameContainingIgnoreCase(name);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create new course (DTO)
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        try {
            Course course = CourseMapper.toEntity(courseDTO);

            // Validate faculty exists
            if (courseDTO.getFacultyId() != null) {
                Optional<Faculty> faculty = facultyRepository.findById(courseDTO.getFacultyId());
                if (faculty.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                course.setFaculty(faculty.get());
            }

            // Validate professor exists
            if (courseDTO.getProfessorId() != null) {
                Optional<Professor> professor = professorRepository.findById(courseDTO.getProfessorId());
                if (professor.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                course.setProfessor(professor.get());
            }

            // Check if course name already exists
            if (courseRepository.existsByCourseName(course.getCourseName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Course savedCourse = courseRepository.save(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(CourseMapper.toDTO(savedCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT update course (DTO)
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        try {
            Optional<Course> existingCourse = courseRepository.findById(id);
            if (existingCourse.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Course currentCourse = existingCourse.get();
            
            // Update fields
            currentCourse.setCourseName(courseDTO.getCourseName());
            currentCourse.setLevel(courseDTO.getLevel());
            
            // Update faculty if provided
            if (courseDTO.getFacultyId() != null) {
                Optional<Faculty> faculty = facultyRepository.findById(courseDTO.getFacultyId());
                if (faculty.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                currentCourse.setFaculty(faculty.get());
            }

            // Update professor if provided
            if (courseDTO.getProfessorId() != null) {
                Optional<Professor> professor = professorRepository.findById(courseDTO.getProfessorId());
                if (professor.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                currentCourse.setProfessor(professor.get());
            }

            Course updatedCourse = courseRepository.save(currentCourse);
            return ResponseEntity.ok(CourseMapper.toDTO(updatedCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE course
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            if (!courseRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            courseRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE course by exact name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteCourseByName(@PathVariable String name) {
        try {
            long deleted = courseRepository.deleteByCourseName(name);
            if (deleted == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE courses by faculty
    @DeleteMapping("/faculty/{facultyId}")
    public ResponseEntity<Long> deleteCoursesByFaculty(@PathVariable Long facultyId) {
        try {
            long deleted = courseRepository.deleteByFacultyFacultyId(facultyId);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE courses by professor
    @DeleteMapping("/professor/{professorId}")
    public ResponseEntity<Long> deleteCoursesByProfessor(@PathVariable Long professorId) {
        try {
            long deleted = courseRepository.deleteByProfessorProfessorId(professorId);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE courses by level
    @DeleteMapping("/level/{level}")
    public ResponseEntity<Long> deleteCoursesByLevel(@PathVariable int level) {
        try {
            long deleted = courseRepository.deleteByLevel(level);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET count of courses by faculty
    @GetMapping("/count/faculty/{facultyId}")
    public ResponseEntity<Long> getCourseCountByFaculty(@PathVariable Long facultyId) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                long count = courseRepository.countByFaculty(faculty.get());
                return ResponseEntity.ok(count);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET count of courses by professor
    @GetMapping("/count/professor/{professorId}")
    public ResponseEntity<Long> getCourseCountByProfessor(@PathVariable Long professorId) {
        try {
            Optional<Professor> professor = professorRepository.findById(professorId);
            if (professor.isPresent()) {
                long count = courseRepository.countByProfessor(professor.get());
                return ResponseEntity.ok(count);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET count of courses by level
    @GetMapping("/count/level/{level}")
    public ResponseEntity<Long> getCourseCountByLevel(@PathVariable int level) {
        try {
            long count = courseRepository.countByLevel(level);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
