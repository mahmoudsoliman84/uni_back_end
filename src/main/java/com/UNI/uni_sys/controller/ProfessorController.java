package com.UNI.uni_sys.controller;

import com.UNI.uni_sys.dto.ProfessorDTO;
import com.UNI.uni_sys.mapper.ProfessorMapper;
import com.UNI.uni_sys.model.Professor;
import com.UNI.uni_sys.model.Faculty;
import com.UNI.uni_sys.repository.ProfessorRepository;
import com.UNI.uni_sys.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "*")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    // GET all professors (DTO)
    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getAllProfessors() {
        try {
            List<Professor> professors = professorRepository.findAll();
            return ResponseEntity.ok(professors.stream().map(ProfessorMapper::toDTO).toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET professor by ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessorById(@PathVariable Long id) {
        try {
            Optional<Professor> professor = professorRepository.findById(id);
            return professor.map(p -> ResponseEntity.ok(ProfessorMapper.toDTO(p)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET professor by ID with courses
    @GetMapping("/{id}/with-courses")
    public ResponseEntity<Professor> getProfessorWithCourses(@PathVariable Long id) {
        try {
            Optional<Professor> professor = professorRepository.findByIdWithCourses(id);
            return professor.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET all professors with courses
    @GetMapping("/with-courses")
    public ResponseEntity<List<Professor>> getAllProfessorsWithCourses() {
        try {
            List<Professor> professors = professorRepository.findAllWithCourses();
            return ResponseEntity.ok(professors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET professors by faculty
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Professor>> getProfessorsByFaculty(@PathVariable Long facultyId) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                List<Professor> professors = professorRepository.findByFaculty(faculty.get());
                return ResponseEntity.ok(professors);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET professors by name (exact match)
    @GetMapping("/name/{name}")
    public ResponseEntity<Professor> getProfessorByName(@PathVariable String name) {
        try {
            Optional<Professor> professor = professorRepository.findByProfessorName(name);
            return professor.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET professors by name (search)
    @GetMapping("/search")
    public ResponseEntity<List<Professor>> searchProfessorsByName(@RequestParam String name) {
        try {
            List<Professor> professors = professorRepository.findByProfessorNameContainingIgnoreCase(name);
            return ResponseEntity.ok(professors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET professors by faculty and name
    @GetMapping("/faculty/{facultyId}/search")
    public ResponseEntity<List<Professor>> searchProfessorsByFacultyAndName(
            @PathVariable Long facultyId, @RequestParam String name) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                List<Professor> professors = professorRepository.findByFacultyAndProfessorNameContainingIgnoreCase(faculty.get(), name);
                return ResponseEntity.ok(professors);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET professors by teaching level
    @GetMapping("/teaching-level/{level}")
    public ResponseEntity<List<Professor>> getProfessorsByTeachingLevel(@PathVariable int level) {
        try {
            List<Professor> professors = professorRepository.findByTeachingLevel(level);
            return ResponseEntity.ok(professors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create new professor (DTO)
    @PostMapping
    public ResponseEntity<ProfessorDTO> createProfessor(@RequestBody ProfessorDTO professorDTO) {
        try {
            Professor professor = ProfessorMapper.toEntity(professorDTO);

            // Validate faculty exists
            if (professorDTO.getFacultyId() != null) {
                Optional<Faculty> faculty = facultyRepository.findById(professorDTO.getFacultyId());
                if (faculty.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                professor.setFaculty(faculty.get());
            }

            // Check if professor name already exists
            if (professorRepository.existsByProfessorName(professor.getProfessorName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Professor savedProfessor = professorRepository.save(professor);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProfessorMapper.toDTO(savedProfessor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT update professor (DTO)
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> updateProfessor(@PathVariable Long id, @RequestBody ProfessorDTO professorDTO) {
        try {
            Optional<Professor> existingProfessor = professorRepository.findById(id);
            if (existingProfessor.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Professor currentProfessor = existingProfessor.get();
            
            // Update fields
            currentProfessor.setProfessorName(professorDTO.getProfessorName());
            
            // Update faculty if provided
            if (professorDTO.getFacultyId() != null) {
                Optional<Faculty> faculty = facultyRepository.findById(professorDTO.getFacultyId());
                if (faculty.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                currentProfessor.setFaculty(faculty.get());
            }

            Professor updatedProfessor = professorRepository.save(currentProfessor);
            return ResponseEntity.ok(ProfessorMapper.toDTO(updatedProfessor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE professor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        try {
            if (!professorRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            professorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE professor by exact name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteProfessorByName(@PathVariable String name) {
        try {
            Optional<Professor> professorOpt = professorRepository.findByProfessorName(name);
            if (professorOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            professorRepository.delete(professorOpt.get());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE all professors in a faculty (cascades will apply per entity remove)
    @DeleteMapping("/faculty/{facultyId}")
    public ResponseEntity<Integer> deleteProfessorsByFaculty(@PathVariable Long facultyId) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<Professor> professors = professorRepository.findByFaculty(faculty.get());
            if (professors.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            professorRepository.deleteAll(professors);
            return ResponseEntity.ok(professors.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET count of professors by faculty
    @GetMapping("/count/faculty/{facultyId}")
    public ResponseEntity<Long> getProfessorCountByFaculty(@PathVariable Long facultyId) {
        try {
            Optional<Faculty> faculty = facultyRepository.findById(facultyId);
            if (faculty.isPresent()) {
                long count = professorRepository.countByFaculty(faculty.get());
                return ResponseEntity.ok(count);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
