package com.UNI.uni_sys.mapper;

import com.UNI.uni_sys.dto.GradeDTO;
import com.UNI.uni_sys.model.Enrollment;
import com.UNI.uni_sys.model.Grade;

public final class GradeMapper {

	private GradeMapper() {}

	public static GradeDTO toDTO(Grade grade) {
		if (grade == null) {
			return null;
		}
		Long enrollmentId = grade.getEnrollment() != null ? grade.getEnrollment().getEnrollmentId() : null;
		return new GradeDTO(
			grade.getGradeId(),
			enrollmentId,
			grade.getGrade()
		);
	}

	public static Grade toEntity(GradeDTO dto) {
		if (dto == null) {
			return null;
		}
		Grade grade = new Grade();
		grade.setGradeId(dto.getGradeId());
		grade.setGrade(dto.getGrade());
		if (dto.getEnrollmentId() != null) {
			Enrollment enrollment = new Enrollment();
			enrollment.setEnrollmentId(dto.getEnrollmentId());
			grade.setEnrollment(enrollment);
		}
		return grade;
	}
}


