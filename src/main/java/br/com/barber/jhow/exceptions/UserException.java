package br.com.barber.jhow.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public abstract class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Ops,our mistake. Internal Server Error");
        pd.setDetail("contact support");

        return pd;
    }
}
