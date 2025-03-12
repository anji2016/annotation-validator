package com.annotation.validator.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.annotation.validator.annotation.Validate;
import com.annotation.validator.annotation.Validates;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public abstract class BaseValidator<T, A extends Annotation> implements ConstraintValidator<A, T> {

    @Override
    public boolean isValid(T object, ConstraintValidatorContext context) {
        boolean hasErrors = false;

        for (Field field : object.getClass().getDeclaredFields()) {
            List<Validate> annotations = getValidationAnnotations(field);
            if (annotations.isEmpty()) continue;

            field.setAccessible(true);
            try {
                Object value = field.get(object);
                for (Validate annotation : annotations) {
                    if (!isValidField(annotation, value)) {
                        hasErrors = true;
                        addValidationError(context, field.getName(), annotation);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error processing validation rules for field: " + field.getName(), e);
            }
        }

        return !hasErrors;
    }

    private List<Validate> getValidationAnnotations(Field field) {
        List<Validate> annotations = new ArrayList<>();
        if (field.isAnnotationPresent(Validate.class)) {
            annotations.add(field.getAnnotation(Validate.class));
        }
        if (field.isAnnotationPresent(Validates.class)) {
            annotations.addAll(List.of(field.getAnnotation(Validates.class).value()));
        }
        return annotations;
    }

    private boolean isValidField(Validate annotation, Object value) {
        try {
            Method method = this.getClass().getDeclaredMethod(annotation.method(), String.class);
            method.setAccessible(true);
            return (boolean) method.invoke(this, (String) value);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking validation method: " + annotation.method(), e);
        }
    }

    private void addValidationError(ConstraintValidatorContext context, String fieldName, Validate annotation) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(annotation.message() + "|" + annotation.type())
               .addPropertyNode(fieldName)
               .addConstraintViolation();
    }
}


