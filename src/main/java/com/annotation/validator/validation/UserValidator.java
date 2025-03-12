package com.annotation.validator.validation;

import com.annotation.validator.annotation.ValidUser;
import com.annotation.validator.dto.UserDto;

public class UserValidator extends BaseValidator<UserDto, ValidUser> {

    public boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z ]+$");
    }

    public boolean isErrName(String name) {
        return name != null && !name.toLowerCase().contains("priya");
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$");
    }

    public boolean isErrEmail(String email) {
        return email != null && !email.equalsIgnoreCase("error@example.com");
    }

    public boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$");
    }

    public boolean isErrorPassword(String password) {
        return password != null && !password.contains("pass");
    }
    
    public boolean isValidId(Long id) {
        return id > 10;
    }
}

