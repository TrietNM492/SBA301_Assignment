package com.edu.fpt.exception;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class InvalidFieldInputException extends MethodArgumentNotValidException {

    public InvalidFieldInputException(MethodParameter parameter, BindingResult bindingResult) {
        super(parameter, bindingResult);
    }
}
