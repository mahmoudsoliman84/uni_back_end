package com.UNI.uni_sys.controller;

import com.UNI.uni_sys.dto.FacultyDTO;
import com.UNI.uni_sys.mapper.FacultyMapper;
import com.UNI.uni_sys.model.Faculty;
import com.UNI.uni_sys.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/faculties")
@CrossOrigin(origins = "*")
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;

    // GET all faculties (DTO)
    @GetMapping
    public ResponseEntity<List<FacultyDTO>> getAllFaculties() {
        try {
            List<Faculty> faculties = facultyRepository.findAll();
            return ResponseEntity.ok(faculties.stream().map(FacultyMapper::toDTO).toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET faculty by ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<FacultyDTO> getFacultyById(@PathVariable Long id) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(id);
            return faculty.map(f -> ResponseEntity.ok(FacultyMapper.toDTO(f)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET faculty by ID with students
    @GetMapping("/{id}/with-students")
    public ResponseEntity<Faculty> getFacultyWithStudents(@PathVariable Long id) {
        try {
            Optional<Faculty> faculty = facultyRepository.findByIdWithStudents(id);
            return faculty.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET faculty by ID with professors
    @GetMapping("/{id}/with-professors")
    public ResponseEntity<Faculty> getFacultyWithProfessors(@PathVariable Long id) {
        try {
            Optional<Faculty> faculty = facultyRepository.findByIdWithProfessors(id);
            return faculty.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET faculty by ID with courses
    @GetMapping("/{id}/with-courses")
    public ResponseEntity<Faculty> getFacultyWithCourses(@PathVariable Long id) {
        try {
            Optional<Faculty> faculty = facultyRepository.findByIdWithCourses(id);
            return faculty.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET faculty by ID with all details
    @GetMapping("/{id}/with-all-details")
    public ResponseEntity<Faculty> getFacultyWithAllDetails(@PathVariable Long id) {
        try {
            Optional<Faculty> faculty = facultyRepository.findByIdWithAllDetails(id);
            return faculty.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET all faculties with students
    @GetMapping("/with-students")
    public ResponseEntity<List<Faculty>> getAllFacultiesWithStudents() {
        try {
            List<Faculty> faculties = facultyRepository.findAllWithStudents();
            return ResponseEntity.ok(faculties);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET all faculties with professors
    @GetMapping("/with-professors")
    public ResponseEntity<List<Faculty>> getAllFacultiesWithProfessors() {
        try {
            List<Faculty> faculties = facultyRepository.findAllWithProfessors();
            return ResponseEntity.ok(faculties);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET all faculties with courses
    @GetMapping("/with-courses")
    public ResponseEntity<List<Faculty>> getAllFacultiesWithCourses() {
        try {
            List<Faculty> faculties = facultyRepository.findAllWithCourses();
            return ResponseEntity.ok(faculties);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET faculty by name (exact match)
    @GetMapping("/name/{name}")
    public ResponseEntity<Faculty> getFacultyByName(@PathVariable String name) {
        try {
            Optional<Faculty> faculty = facultyRepository.findByFacultyName(name);
            return faculty.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET faculties by name (search)
    @GetMapping("/search")
    public ResponseEntity<List<Faculty>> searchFacultiesByName(@RequestParam String name) {
        try {
            List<Faculty> faculties = facultyRepository.findByFacultyNameContainingIgnoreCase(name);
            return ResponseEntity.ok(faculties);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create new faculty (DTO)
    @PostMapping
    public ResponseEntity<FacultyDTO> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        try {
            // Check if faculty name already exists
            if (facultyRepository.existsByFacultyName(facultyDTO.getFacultyName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Faculty savedFaculty = facultyRepository.save(FacultyMapper.toEntity(facultyDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(FacultyMapper.toDTO(savedFaculty));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT update faculty (DTO)
    @PutMapping("/{id}")
    public ResponseEntity<FacultyDTO> updateFaculty(@PathVariable Long id, @RequestBody FacultyDTO facultyDTO) {
        try {
            Optional<Faculty> existingFaculty = facultyRepository.findById(id);
            if (existingFaculty.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Faculty currentFaculty = existingFaculty.get();
            
            // Check if the new name conflicts with existing faculty (excluding current)
            if (!currentFaculty.getFacultyName().equals(facultyDTO.getFacultyName()) &&
                facultyRepository.existsByFacultyName(facultyDTO.getFacultyName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Update fields
            currentFaculty.setFacultyName(facultyDTO.getFacultyName());

            Faculty updatedFaculty = facultyRepository.save(currentFaculty);
            return ResponseEntity.ok(FacultyMapper.toDTO(updatedFaculty));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE faculty
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        try {
            if (!facultyRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            facultyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE faculty by exact name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteFacultyByName(@PathVariable String name) {
        try {
            long deleted = facultyRepository.deleteByFacultyName(name);
            if (deleted == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
