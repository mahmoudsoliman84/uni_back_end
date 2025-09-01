package com.UNI.uni_sys.mapper;

import com.UNI.uni_sys.dto.EnrollmentDTO;
import com.UNI.uni_sys.model.Course;
import com.UNI.uni_sys.model.Enrollment;
import com.UNI.uni_sys.model.Student;

public final class EnrollmentMapper {

	private EnrollmentMapper() {}

	public static EnrollmentDTO toDTO(Enrollment enrollment) {
		if (enrollment == null) {
			return null;
		}
		Long studentId = enrollment.getStudent() != null ? enrollment.getStudent().getStudentId() : null;
		Long courseId = enrollment.getCourse() != null ? enrollment.getCourse().getCourseId() : null;
		return new EnrollmentDTO(
			enrollment.getEnrollmentId(),
			studentId,
			courseId
		);
	}

	public static Enrollment toEntity(EnrollmentDTO dto) {
		if (dto == null) {
			return null;
		}
		Enrollment enrollment = new Enrollment();
		enrollment.setEnrollmentId(dto.getEnrollmentId());
		if (dto.getStudentId() != null) {
			Student student = new Student();
			student.setStudentId(dto.getStudentId());
			enrollment.setStudent(student);
		}
		if (dto.getCourseId() != null) {
			Course course = new Course();
			course.setCourseId(dto.getCourseId());
			enrollment.setCourse(course);
		}
		return enrollment;
	}
}


