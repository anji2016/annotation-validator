package com.annotation.validator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.validator.dto.StudentDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid StudentDto studentDto) {
        return ResponseEntity.ok("Success");
    }

}
