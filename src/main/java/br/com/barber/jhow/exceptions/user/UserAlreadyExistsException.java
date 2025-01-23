package br.com.barber.jhow.exceptions.user;

import br.com.barber.jhow.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserAlreadyExistsException extends UserException {

    private String detail;

    public UserAlreadyExistsException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("User already exists");
        pd.setDetail(detail);

        return pd;
    }
}
