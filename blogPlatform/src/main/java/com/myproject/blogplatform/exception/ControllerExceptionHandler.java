package com.myproject.blogplatform.exception;

import com.myproject.blogplatform.model.dto.ResponseError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
/**
 Обработчик исключения контроллеров.
 */

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({HttpClientErrorException.class, MissingServletRequestParameterException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleIllegalArgumentException(
            Exception ex, WebRequest request) {
        log.error("Error 400: Bad Request");
        return new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({
            NoHandlerFoundException.class,
            ArticleNotFoundException.class,
            AuthorNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleNoResourceFoundException(RuntimeException ex, WebRequest request) {
        log.error("Error 404: Not Found");
        return new ResponseError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseError handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        log.error("Error 405: Method not supported");
        return new ResponseError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleAllExceptions(RuntimeException ex, WebRequest request) {
        log.error("Error 500: Internal Server Error");
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
    @ExceptionHandler({ArticleTakenException.class,AuthorTakenException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleConflictExceptions(Exception ex, WebRequest request){
        log.error("Error 409: Conflict");
        return new ResponseError(HttpStatus.CONFLICT, ex.getMessage());
    }
}

