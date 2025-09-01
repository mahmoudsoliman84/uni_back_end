package com.UNI.uni_sys.mapper;

import com.UNI.uni_sys.dto.StudentDTO;
import com.UNI.uni_sys.model.Faculty;
import com.UNI.uni_sys.model.Student;

public final class StudentMapper {

	private StudentMapper() {}

	public static StudentDTO toDTO(Student student) {
		if (student == null) {
			return null;
		}
		Long facultyId = student.getFaculty() != null ? student.getFaculty().getFacultyId() : null;
		return new StudentDTO(
			student.getStudentId(),
			student.getStudentName(),
			student.getLevel(),
			facultyId
		);
	}

	public static Student toEntity(StudentDTO dto) {
		if (dto == null) {
			return null;
		}
		Student student = new Student();
		student.setStudentId(dto.getStudentId());
		student.setStudentName(dto.getStudentName());
		student.setLevel(dto.getLevel());
		if (dto.getFacultyId() != null) {
			Faculty faculty = new Faculty();
			faculty.setFacultyId(dto.getFacultyId());
			student.setFaculty(faculty);
		}
		return student;
	}
}


