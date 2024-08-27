package org.daniel.error;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ElasticsearchException.class)
    public ErrorResult esErrorHandle(ElasticsearchException e){
        log.warn("The es is unavailable, the error is: ", e);
        return ErrorResult.of(ErrorEnum.ES_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResult unknownErrorHandler(Exception e){
        log.error("Encounter unknown error: ", e);
        return ErrorResult.of(ErrorEnum.UNKNOWN_ERROR);
    }
}
