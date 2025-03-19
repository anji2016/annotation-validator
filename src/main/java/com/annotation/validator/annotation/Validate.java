package com.annotation.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.annotation.validator.enums.Type;

@Repeatable(Validates.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validate {
	Class<?> validatorClass();  // Specify the validator class
    String method();            // The name of the method (retrieved dynamically)
    String message();           // The error message
    Type type() default Type.ERROR;
}
