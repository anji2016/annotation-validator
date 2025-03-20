package com.annotation.validator.dto;

import com.annotation.validator.annotation.Validate;
import com.annotation.validator.annotation.ValidateDTO;
import com.annotation.validator.enums.Type;
import com.annotation.validator.validation.GlobalValidator;
import com.annotation.validator.validation.UserValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ValidateDTO
public class UserDto {
	
	@NotBlank(message = "Name is required")
	@NotNull(message = "Name cannot be null")
	@Validate(validatorClass = UserValidator.class,method = "isValidName", message = "%s must contain only alphabets and spaces", type = Type.WARN)
	@Validate(validatorClass = UserValidator.class,method = "isErrName", message = "%s must not contain 'priya'", type = Type.ERROR)
	private String name;

	@NotBlank(message = "Email is required")
	@Validate(validatorClass = UserValidator.class,method = "isValidEmail", message = "Invalid email format", type = Type.ERROR)
	@Validate(validatorClass = UserValidator.class,method = "isErrEmail", message = "It is an Error Email", type = Type.WARN)
	private String email;

	@NotBlank(message = "Password is required")
	@Validate(validatorClass = GlobalValidator.class, method = "isValidPassword", message = "Password must contain an uppercase letter, digit, and special character", type = Type.ERROR)
	@Validate(validatorClass = UserValidator.class, method = "isErrorPassword", message = "Password should contain 'pass'", type = Type.WARN)
	private String password;
	
	@NotNull(message = "Id cannot be null")
	@Validate(validatorClass = GlobalValidator.class, method = "isValidId", message = "%d Id should be greater than 10", type = Type.ERROR)
	private Long id;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Long getId() {
		return id;
	}

}
