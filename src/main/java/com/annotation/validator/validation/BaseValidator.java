package com.annotation.validator.validation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.annotation.validator.annotation.TriggerAnnotation;
import com.annotation.validator.annotation.Validate;
import com.annotation.validator.annotation.Validates;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BaseValidator implements ConstraintValidator<TriggerAnnotation, Object> {

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

	private boolean isValidField(Validate annotation, Object object) {
		try {
			// Check if validatorClass is specified
			if (annotation.validatorClass() != void.class) {
				return invokeExternalValidator(annotation, object);
			}
			return invokeInternalValidator(annotation, object);
		} catch (Exception e) {
			throw new RuntimeException("Error invoking validation method: " + annotation.method(), e);
		}
	}

	private boolean invokeExternalValidator(Validate annotation, Object object) throws Exception {
		Class<?> validatorClass = annotation.validatorClass();

		// Create and autowire the validator instance
		Object validatorInstance = createAndAutowireInstance(validatorClass);

		// Get the validation method
		Method method = validatorClass.getDeclaredMethod(annotation.method(), object.getClass());
		method.setAccessible(true);

		// Invoke the validation method
		return (boolean) method.invoke(validatorInstance, object);
	}

	private boolean invokeInternalValidator(Validate annotation, Object object) throws Exception {
		// Use internal validator method
		Method method = this.getClass().getDeclaredMethod(annotation.method(), object.getClass());
		method.setAccessible(true);

		// Invoke the validation method
		return (boolean) method.invoke(this, object);
	}

	private void addValidationError(ConstraintValidatorContext context, String fieldName, Validate annotation,
			Object object) {
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
		// Create a new instance using reflection
		Object instance = validatorClass.getDeclaredConstructor().newInstance();

		// Manually inject dependencies using Spring's AutowireCapableBeanFactory
		AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
		factory.autowireBean(instance);
		factory.initializeBean(instance, validatorClass.getSimpleName());

		return instance;
	}
}
