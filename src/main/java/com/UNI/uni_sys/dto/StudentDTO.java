package com.UNI.uni_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

	private Long studentId;

	private String studentName;

	private int level;

	private Long facultyId;
}


