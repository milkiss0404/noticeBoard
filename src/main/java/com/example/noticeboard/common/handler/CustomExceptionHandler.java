package com.example.noticeboard.common.handler;
import com.example.noticeboard.common.exception.CustomBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomExceptionHandler {

        @ResponseBody
        @ExceptionHandler(CustomBadRequestException.class)
        public ResponseEntity<String> handleCustomBadRequestException(CustomBadRequestException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
}
