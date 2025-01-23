package br.com.barber.jhow.exceptions.user;

import br.com.barber.jhow.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserBadCredentialsException extends UserException {

    private String detail;

    public UserBadCredentialsException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        pd.setDetail("Verify your email or password");
        pd.setDetail(detail);

        return pd;
    }
}
