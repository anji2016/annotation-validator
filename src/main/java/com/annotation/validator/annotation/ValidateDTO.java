package com.annotation.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.annotation.validator.validation.CoreValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = CoreValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateDTO {
	String message() default "Invalid user data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
