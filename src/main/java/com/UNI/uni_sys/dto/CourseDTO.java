package com.UNI.uni_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

	private Long courseId;

	private String courseName;

	private int level;

	private Long facultyId;

	private Long professorId;
}


