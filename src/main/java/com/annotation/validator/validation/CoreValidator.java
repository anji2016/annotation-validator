package com.annotation.validator.validation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.annotation.validator.annotation.Validate;
import com.annotation.validator.annotation.ValidateDTO;
import com.annotation.validator.annotation.Validates;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CoreValidator implements ConstraintValidator<ValidateDTO, Object> {

    @Autowired
    private ApplicationContext applicationContext; // Inject Spring Context

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean hasErrors = false;

        for (Field field : object.getClass().getDeclaredFields()) {
            List<Validate> annotations = getValidationAnnotations(field);
            if (annotations.isEmpty())
                continue;

            for (Validate annotation : annotations) {
                if (!isValidField(annotation, object, field)) {
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

    private boolean isValidField(Validate annotation, Object object, Field field) {
        try {
            Class<?> validatorClass = annotation.validatorClass();
            Object validatorInstance = createAndAutowireInstance(validatorClass);

            // Get the field value
            field.setAccessible(true);
            Object fieldValue = field.get(object);

            // Find the correct method
            Method validationMethod = findValidationMethod(validatorClass, annotation.method(), object, fieldValue);

            if (validationMethod == null) {
                throw new NoSuchMethodException("No matching method found: " + annotation.method());
            }

            validationMethod.setAccessible(true);

            // Determine the correct argument (DTO or field value)
            Object argument = validationMethod.getParameterTypes()[0].isAssignableFrom(object.getClass()) ? object : fieldValue;

            return (boolean) validationMethod.invoke(validatorInstance, argument);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking validation method: " + annotation.method(), e);
        }
    }

    private Method findValidationMethod(Class<?> validatorClass, String methodName, Object dto, Object fieldValue) {
        for (Method method : validatorClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                if (paramType.isAssignableFrom(dto.getClass()) || paramType.isAssignableFrom(fieldValue.getClass())) {
                    return method;
                }
            }
        }
        return null;
    }

    private void addValidationError(ConstraintValidatorContext context, String fieldName, Validate annotation, Object object) {
        context.disableDefaultConstraintViolation();

        // Format the message dynamically with the field value if needed
        String formattedMessage = String.format(annotation.message(), getFieldValue(object, fieldName));

        context.buildConstraintViolationWithTemplate(formattedMessage + "|" + annotation.type())
                .addPropertyNode(fieldName).addConstraintViolation();
    }

    private Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            return "Unknown"; // Fallback in case of error
        }
    }

    /**
     * Creates a new instance of the validator class and autowires dependencies.
     */
    private Object createAndAutowireInstance(Class<?> validatorClass) throws Exception {
        Object instance = validatorClass.getDeclaredConstructor().newInstance();
        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        factory.autowireBean(instance);
        factory.initializeBean(instance, validatorClass.getSimpleName());
        return instance;
    }
}

