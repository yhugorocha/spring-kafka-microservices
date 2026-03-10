package io.github.yhugorocha.orders.exception;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleResourceNotFound(ResourceNotFoundException exception,
                                                   ServletWebRequest request) {
        return this.buildErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequest().getRequestURI(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                         ServletWebRequest request) {
        var fieldErrors = new LinkedHashMap<String, String>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return this.buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Dados da requisição são inválidos",
                request.getRequest().getRequestURI(),
                fieldErrors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMessageNotReadable(HttpMessageNotReadableException exception,
                                                     ServletWebRequest request) {
        return this.buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido",
                request.getRequest().getRequestURI(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleUnexpectedException(Exception exception,
                                                      ServletWebRequest request) {
        return this.buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno no processamento da requisição",
                request.getRequest().getRequestURI(),
                null
        );
    }

    private ApiErrorResponse buildErrorResponse(HttpStatus status,
                                                String message,
                                                String path,
                                                LinkedHashMap<String, String> fieldErrors) {
        return ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors)
                .build();
    }
}
