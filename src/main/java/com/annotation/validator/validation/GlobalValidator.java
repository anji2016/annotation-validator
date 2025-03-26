package com.annotation.validator.validation;

import com.annotation.validator.dto.UserDto;

public class GlobalValidator {

	public boolean isValidId(Long id) {
		return id > 10;
	}
	
	public boolean isValidPassword(UserDto dto) {
        return dto.getPassword() != null && dto.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$");
    }

}
