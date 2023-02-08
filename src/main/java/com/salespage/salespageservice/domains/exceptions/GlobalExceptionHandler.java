package com.salespage.salespageservice.domains.exceptions;

import com.mongodb.DuplicateKeyException;
import com.salespage.salespageservice.domains.exceptions.info.BaseException;
import com.salespage.salespageservice.domains.exceptions.info.ErrorCode;
import com.salespage.salespageservice.domains.exceptions.info.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.NestedServletException;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceExitsException.class})
    public ResponseEntity<Object> handleFoundException(BaseException exception, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BaseException exception, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccountNotExistsException.class, ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(BaseException exception, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ExpiredJwtException.class, WrongAccountOrPasswordException.class, UnauthorizedException.class, MalformedJwtException.class, NestedServletException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleUnAuthorizedException(BaseException exception, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity<Object> handleUnAuthorizationException(BaseException exception, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(BaseException exception, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<Object> handleDuplicateKeyException(BaseException exception, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(new BaseException(ErrorCode.NOT_VALID, ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse.createFrom(new BaseException(ErrorCode.BAD_REQUEST, ex.getMessage())), HttpStatus.BAD_REQUEST);

    }
}



