package com.sd.service.todolist.boundary;

import com.sd.service.todolist.exception.DataPreconditionException;
import com.sd.service.todolist.exception.StorageException;
import com.sd.service.todolist.model.error.BasicErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ServiceExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StorageException.DataIntegrityException.class)
    public @ResponseBody BasicErrorResponse handle(StorageException.DataIntegrityException exception) {
        log.warn(exception.getMessage(), exception);
        return new BasicErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StorageException.DataNotFoundException.class)
    public @ResponseBody BasicErrorResponse handle(StorageException.DataNotFoundException exception) {
        log.warn(exception.getMessage(), exception);
        return new BasicErrorResponse(HttpStatus.NOT_FOUND.toString(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(DataPreconditionException.class)
    public @ResponseBody BasicErrorResponse handle(DataPreconditionException exception) {
        log.warn(exception.getMessage(), exception);
        return new BasicErrorResponse(HttpStatus.PRECONDITION_FAILED.toString(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public @ResponseBody BasicErrorResponse handle(Exception exception) {
        log.error("Unhandled exception occurred. The client receives HTTP-500.", exception);
        return new BasicErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception.getMessage());
    }

}
