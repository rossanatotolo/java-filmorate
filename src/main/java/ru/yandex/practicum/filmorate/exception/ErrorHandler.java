package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class) //400
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        log.debug("Получен статус 400 Bad Request {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler //400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler //400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler //404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.debug("Получен статус 404 Not Found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler //409
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedData(final DuplicatedDataException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler //500
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }
}


//package ru.yandex.practicum.filmorate.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import jakarta.validation.ConstraintViolationException;
//
//@RestControllerAdvice
//@Slf4j
//public class ErrorHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
//        log.debug("Validation error: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
//        log.debug("Constraint violation: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(DuplicatedDataException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<String> handleDuplicatedDataException(DuplicatedDataException ex) {
//        log.debug("Duplicated data: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    // Обработчик для прочих исключений
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<String> handleAllExceptions(Exception ex) {
//        log.error("Internal server error: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//
//import java.util.Map;
//
//@Slf4j
//@RestControllerAdvice
//public class ErrorHandler {
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public Map<String, String> handleNotFound(final NotFoundException exception) {
//        log.debug("Получен статус 404 Not found {}", exception.getMessage(), exception);
//        return Map.of("error", exception.getMessage());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Map<String, String> handleValidation(final ValidationException exception) {
//        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
//        return Map.of("error", exception.getMessage());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Map<String, String> handlerMethodArgumentNotValid(MethodArgumentNotValidException exception) {
//        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
//        return Map.of("error", exception.getMessage());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Map<String, String> handlerInternal(final Exception exception) {
//        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
//        return Map.of("error", exception.getMessage());
//    }
//}