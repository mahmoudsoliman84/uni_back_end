package com.UNI.uni_sys.controller;

import com.UNI.uni_sys.dto.StudentDTO;
import com.UNI.uni_sys.mapper.StudentMapper;
import com.UNI.uni_sys.model.Student;
import com.UNI.uni_sys.model.Faculty;
import com.UNI.uni_sys.repository.StudentRepository;
import com.UNI.uni_sys.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    // GET all students (DTO)
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        try {
            List<Student> students = studentRepository.findAll();
            return ResponseEntity.ok(students.stream().map(StudentMapper::toDTO).toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET student by ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        try {
            Optional<Student> student = studentRepository.findById(id);
            return student.map(s -> ResponseEntity.ok(StudentMapper.toDTO(s)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET student by ID with enrollments
    @GetMapping("/{id}/with-enrollments")
    public ResponseEntity<Student> getStudentWithEnrollments(@PathVariable Long id) {
        try {
            Optional<Student> student = studentRepository.findByIdWithEnrollments(id);
            return student.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET students by faculty
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Student>> getStudentsByFaculty(@PathVariable Long facultyId) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                List<Student> students = studentRepository.findByFaculty(faculty.get());
                return ResponseEntity.ok(students);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET students by level
    @GetMapping("/level/{level}")
    public ResponseEntity<List<Student>> getStudentsByLevel(@PathVariable int level) {
        try {
            List<Student> students = studentRepository.findByLevel(level);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET students by faculty and level
    @GetMapping("/faculty/{facultyId}/level/{level}")
    public ResponseEntity<List<Student>> getStudentsByFacultyAndLevel(
            @PathVariable Long facultyId, @PathVariable int level) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                List<Student> students = studentRepository.findByFacultyAndLevel(faculty.get(), level);
                return ResponseEntity.ok(students);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET students by name (search)
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudentsByName(@RequestParam String name) {
        try {
            List<Student> students = studentRepository.findByStudentNameContainingIgnoreCase(name);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create new student (DTO)
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        try {
            Student student = StudentMapper.toEntity(studentDTO);

            // Validate faculty exists
            if (studentDTO.getFacultyId() != null) {
                Optional<Faculty> faculty = facultyRepository.findById(studentDTO.getFacultyId());
                if (faculty.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                student.setFaculty(faculty.get());
            }

            // Check if student name already exists
            if (studentRepository.existsByStudentName(student.getStudentName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Student savedStudent = studentRepository.save(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(StudentMapper.toDTO(savedStudent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT update student (DTO)
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        try {
            Optional<Student> existingStudent = studentRepository.findById(id);
            if (existingStudent.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Student currentStudent = existingStudent.get();
            
            // Update fields
            currentStudent.setStudentName(studentDTO.getStudentName());
            currentStudent.setLevel(studentDTO.getLevel());
            
            // Update faculty if provided
            if (studentDTO.getFacultyId() != null) {
                Optional<Faculty> faculty = facultyRepository.findById(studentDTO.getFacultyId());
                if (faculty.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                currentStudent.setFaculty(faculty.get());
            }

            Student updatedStudent = studentRepository.save(currentStudent);
            return ResponseEntity.ok(StudentMapper.toDTO(updatedStudent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            if (!studentRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE student by exact name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteStudentByName(@PathVariable String name) {
        try {
            long deleted = studentRepository.deleteByStudentName(name);
            if (deleted == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE students by faculty
    @DeleteMapping("/faculty/{facultyId}")
    public ResponseEntity<Long> deleteStudentsByFaculty(@PathVariable Long facultyId) {
        try {
            long deleted = studentRepository.deleteByFacultyFacultyId(facultyId);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE students by level
    @DeleteMapping("/level/{level}")
    public ResponseEntity<Long> deleteStudentsByLevel(@PathVariable int level) {
        try {
            long deleted = studentRepository.deleteByLevel(level);
            if (deleted == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET count of students by faculty
    @GetMapping("/count/faculty/{facultyId}")
    public ResponseEntity<Long> getStudentCountByFaculty(@PathVariable Long facultyId) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                long count = studentRepository.countByFaculty(faculty.get());
                return ResponseEntity.ok(count);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET count of students by level
    @GetMapping("/count/level/{level}")
    public ResponseEntity<Long> getStudentCountByLevel(@PathVariable int level) {
        try {
            long count = studentRepository.countByLevel(level);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
