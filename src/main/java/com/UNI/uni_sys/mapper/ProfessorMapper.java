package com.UNI.uni_sys.mapper;

import com.UNI.uni_sys.dto.ProfessorDTO;
import com.UNI.uni_sys.model.Faculty;
import com.UNI.uni_sys.model.Professor;

public final class ProfessorMapper {

	private ProfessorMapper() {}

	public static ProfessorDTO toDTO(Professor professor) {
		if (professor == null) {
			return null;
		}
		Long facultyId = professor.getFaculty() != null ? professor.getFaculty().getFacultyId() : null;
		return new ProfessorDTO(
			professor.getProfessorId(),
			professor.getProfessorName(),
			facultyId
		);
	}

	public static Professor toEntity(ProfessorDTO dto) {
		if (dto == null) {
			return null;
		}
		Professor professor = new Professor();
		professor.setProfessorId(dto.getProfessorId());
		professor.setProfessorName(dto.getProfessorName());
		if (dto.getFacultyId() != null) {
			Faculty faculty = new Faculty();
			faculty.setFacultyId(dto.getFacultyId());
			professor.setFaculty(faculty);
		}
		return professor;
	}
}


