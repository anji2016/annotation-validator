package com.annotation.validator.dto;

import com.annotation.validator.annotation.ValidUser;
import com.annotation.validator.annotation.Validate;
import com.annotation.validator.annotation.Validates;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.annotation.validator.enums.Type;

@ValidUser
public class UserDto {
	
	@NotBlank(message = "Name is required")
	@NotNull(message = "Name cannot be null")
	@Validates({
        @Validate(method = "isValidName", message = "Name must contain only alphabets and spaces", type = Type.WARN),
        @Validate(method = "isErrName", message = "Name must not contain 'priya'", type = Type.ERROR)
    })
	private String name;

	@NotBlank(message = "Email is required")
	 @Validates({
	        @Validate(method = "isValidEmail", message = "Invalid email format", type = Type.ERROR),
	        @Validate(method = "isErrEmail", message = "It is an Error Email", type = Type.WARN)
	    })
	private String email;

	@NotBlank(message = "Password is required")
	@Validates({
        @Validate(method = "isValidPassword", message = "Password must contain an uppercase letter, digit, and special character", type = Type.ERROR),
        @Validate(method = "isErrorPassword", message = "Password should contain 'pass'", type = Type.WARN),
    })
	private String password;
	
	@NotNull(message = "Id cannot be null")
	@Validates({
        @Validate(method = "isValidId", message = "Id should be greater than 10", type = Type.ERROR),
    })
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
