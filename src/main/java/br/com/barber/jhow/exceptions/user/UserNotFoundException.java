package br.com.barber.jhow.exceptions.user;

import br.com.barber.jhow.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotFoundException extends UserException {

    private final String detail;

    public UserNotFoundException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("User not found");
        pd.setDetail(detail);

        return pd;
    }
}
