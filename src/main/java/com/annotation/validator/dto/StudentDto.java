package com.annotation.validator.dto;

import com.annotation.validator.annotation.Validate;
import com.annotation.validator.annotation.ValidateDTO;
import com.annotation.validator.enums.Type;
import com.annotation.validator.validation.GlobalValidator;

import jakarta.validation.constraints.NotNull;

@ValidateDTO
public class StudentDto {
	
	@NotNull(message = "StudentId cannot be null")
	@Validate(validatorClass = GlobalValidator.class, method = "isValidId", message = "%d Id should be greater than 10", type = Type.ERROR)
	private Long studentId;

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	
}
