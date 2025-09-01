package com.UNI.uni_sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {

	private Long gradeId;

	private Long enrollmentId;

	private Double grade;
}


