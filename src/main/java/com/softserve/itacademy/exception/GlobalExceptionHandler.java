package com.softserve.itacademy.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NullEntityReferenceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView nullEntityReferenceExceptionHandler(HttpServletRequest request, NullEntityReferenceException exception) {
        return getModelAndView(request, HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public ModelAndView entityNotFoundExceptionHandler(HttpServletRequest request, EntityNotFoundException exception) {
        return getModelAndView(request, HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value= HttpStatus.FORBIDDEN)
    public ModelAndView accessDeniedExceptionHandler(HttpServletRequest request, AccessDeniedException exception) {
        ModelAndView modelAndView = new ModelAndView("error/403");
        modelAndView.addObject("code", HttpStatus.FORBIDDEN);
        modelAndView.addObject("message", exception.getMessage());
        return modelAndView;
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView internalServerErrorHandler(HttpServletRequest request, Exception exception) {
//        return getModelAndView(request, HttpStatus.INTERNAL_SERVER_ERROR, exception);
//    }

    private ModelAndView getModelAndView(HttpServletRequest request, HttpStatus httpStatus, Exception exception) {
        logger.error("Exception raised = {} :: URL = {}", exception.getMessage(), request.getRequestURL());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", httpStatus.value() + " / " + httpStatus.getReasonPhrase());
        modelAndView.addObject("message", exception.getMessage());
        return modelAndView;
    }
}
