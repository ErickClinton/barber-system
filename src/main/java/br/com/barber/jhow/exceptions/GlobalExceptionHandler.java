package br.com.barber.jhow.exceptions;

import br.com.barber.jhow.exceptions.dto.InvalidParamDto;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ProblemDetail handleUserException(UserException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleException(MethodArgumentNotValidException e) {
        var pd =  ProblemDetail.forStatus(400);
        var invalidParam = e.getFieldErrors()
                .stream()
                .map(fe -> new InvalidParamDto(fe.getField(), fe.getDefaultMessage()))
                .toList();

        pd.setTitle("Invalid request param");
        pd.setDetail("There is invalid fields on the request");
        pd.setProperty("invalid params", invalidParam);

        return pd;

    }
}
