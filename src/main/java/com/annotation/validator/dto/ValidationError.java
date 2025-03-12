package com.annotation.validator.dto;

import java.util.ArrayList;
import java.util.List;

import com.annotation.validator.enums.Type;

public class ValidationError {
    private String field;
    private List<String> messages = new ArrayList<>(); // Ensure it's initialized
    private Type type;
    
    public ValidationError(String field, List<String> messages,Type type) {
		super();
		this.field = field;
		this.messages = messages;
		this.type = type;
	}

	public ValidationError(String field,Type type) {
        this.field = field;
        this.messages = new ArrayList<>();
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
    
}
