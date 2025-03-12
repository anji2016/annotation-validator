package com.annotation.validator.service;

import org.springframework.stereotype.Service;

import com.annotation.validator.dto.UserDto;

import jakarta.validation.Valid;

@Service
public class UserService {
	public String registerUser(@Valid UserDto userDTO) {
		return "User registered successfully: " + userDTO.getEmail();
	}
}