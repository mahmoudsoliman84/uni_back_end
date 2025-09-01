package com.UNI.uni_sys.mapper;

import com.UNI.uni_sys.dto.CourseDTO;
import com.UNI.uni_sys.model.Course;
import com.UNI.uni_sys.model.Faculty;
import com.UNI.uni_sys.model.Professor;

public final class CourseMapper {

	private CourseMapper() {}

	public static CourseDTO toDTO(Course course) {
		if (course == null) {
			return null;
		}
		Long facultyId = course.getFaculty() != null ? course.getFaculty().getFacultyId() : null;
		Long professorId = course.getProfessor() != null ? course.getProfessor().getProfessorId() : null;
		return new CourseDTO(
			course.getCourseId(),
			course.getCourseName(),
			course.getLevel(),
			facultyId,
			professorId
		);
	}

	public static Course toEntity(CourseDTO dto) {
		if (dto == null) {
			return null;
		}
		Course course = new Course();
		course.setCourseId(dto.getCourseId());
		course.setCourseName(dto.getCourseName());
		course.setLevel(dto.getLevel());
		if (dto.getFacultyId() != null) {
			Faculty faculty = new Faculty();
			faculty.setFacultyId(dto.getFacultyId());
			course.setFaculty(faculty);
		}
		if (dto.getProfessorId() != null) {
			Professor professor = new Professor();
			professor.setProfessorId(dto.getProfessorId());
			course.setProfessor(professor);
		}
		return course;
	}
}


