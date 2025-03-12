package com.annotation.validator.validation;

import java.util.function.Predicate;
import com.annotation.validator.enums.Type;

public class ValidationRule<T> {
	private final String field;
	private final T value;
	private final String message;
	private final Predicate<T> predicate;
	private Type type;

	public ValidationRule(String field, T value, String message, Predicate<T> predicate) {
		this.field = field;
		this.value = value;
		this.message = message;
		this.predicate = predicate;
		this.type = Type.ERROR;
	}

	public ValidationRule(String field, T value, String message, Predicate<T> predicate, Type type) {
		this.field = field;
		this.value = value;
		this.message = message;
		this.predicate = predicate;
		this.type = type;
	}

	public boolean isInvalid() {
		return value == null || !predicate.test(value);
	}

	public String getField() {
		return field;
	}

	public T getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}

	public Predicate<T> getPredicate() {
		return predicate;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
