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

            for (Validate annotation : annotations) {
                if (!isValidField(annotation, object)) {
                    hasErrors = true;
                    addValidationError(context, field.getName(), annotation, object);
                }
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

    private boolean isValidField(Validate annotation, T object) {
        try {
            Class<?> validatorClass = annotation.validatorClass(); // Get the specified validator class
            Object validatorInstance = validatorClass.getDeclaredConstructor().newInstance();
            
            Method method = validatorClass.getDeclaredMethod(annotation.method(), object.getClass());
            method.setAccessible(true);
            return (boolean) method.invoke(validatorInstance, object);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking validation method: " + annotation.method(), e);
        }
    }

    private void addValidationError(ConstraintValidatorContext context, String fieldName, Validate annotation, T object) {
        context.disableDefaultConstraintViolation();

        // Format the message dynamically with the field value if needed
        String formattedMessage = String.format(annotation.message(), getFieldValue(object, fieldName));

        context.buildConstraintViolationWithTemplate(formattedMessage + "|" + annotation.type())
               .addPropertyNode(fieldName)
               .addConstraintViolation();
    }

    private Object getFieldValue(T object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            return "Unknown"; // Fallback in case of error
        }
    }
}



