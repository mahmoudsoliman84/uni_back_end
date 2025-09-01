package com.UNI.uni_sys.mapper;

import com.UNI.uni_sys.dto.FacultyDTO;
import com.UNI.uni_sys.model.Faculty;

public final class FacultyMapper {

	private FacultyMapper() {}

	public static FacultyDTO toDTO(Faculty faculty) {
		if (faculty == null) {
			return null;
		}
		return new FacultyDTO(
			faculty.getFacultyId(),
			faculty.getFacultyName()
		);
	}

	public static Faculty toEntity(FacultyDTO dto) {
		if (dto == null) {
			return null;
		}
		Faculty faculty = new Faculty();
		faculty.setFacultyId(dto.getFacultyId());
		faculty.setFacultyName(dto.getFacultyName());
		return faculty;
	}
}


