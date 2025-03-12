package com.annotation.validator.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.annotation.validator.validation.UserValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;

@Constraint(validatedBy = UserValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUser {
	String message() default "Invalid user data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
