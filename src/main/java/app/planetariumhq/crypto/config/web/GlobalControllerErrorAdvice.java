package app.planetariumhq.crypto.config.web;

import app.planetariumhq.crypto.support.domain.DomainValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalControllerErrorAdvice {

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> domainValidationException(DomainValidationException e) {
        log.error(e.getMessage(), e);

        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.ERROR(e.getCode(), e.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

}
