package com.UNI.uni_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {

	private Long enrollmentId;

	private Long studentId;

	private Long courseId;
}


