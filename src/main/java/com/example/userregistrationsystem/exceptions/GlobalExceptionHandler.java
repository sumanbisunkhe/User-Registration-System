package com.example.userregistrationsystem.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handler for UsernameAlreadyExistsException
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ModelAndView handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.setViewName("users/error");  // This will render the error.html page
        return mav;
    }

    // Handler for other exceptions (optional)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        mav.setViewName("users/error");
        return mav;
    }
}
