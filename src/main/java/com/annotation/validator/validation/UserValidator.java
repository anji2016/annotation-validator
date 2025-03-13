package com.annotation.validator.validation;

import com.annotation.validator.annotation.ValidUser;
import com.annotation.validator.dto.UserDto;

public class UserValidator extends BaseValidator<UserDto, ValidUser> {

    public boolean isValidName(UserDto dto) {
        return dto.getName() != null && dto.getName().matches("^[a-zA-Z ]+$");
    }

    public boolean isErrName(UserDto dto) {
        return dto.getName() != null && !dto.getName().toLowerCase().contains("priya");
    }

    public boolean isValidEmail(UserDto dto) {
        return dto.getEmail() != null && dto.getEmail().matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$");
    }

    public boolean isErrEmail(UserDto dto) {
        return dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase("error@example.com");
    }

    public boolean isValidPassword(UserDto dto) {
        return dto.getPassword() != null && dto.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$");
    }

    public boolean isErrorPassword(UserDto dto) {
        return dto.getPassword() != null && !dto.getPassword().contains("pass");
    }
    
    public boolean isValidId(UserDto dto) {
        return dto.getId() > 10;
    }
}

