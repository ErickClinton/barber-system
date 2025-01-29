package br.com.barber.jhow.exceptions.scheduling;

import br.com.barber.jhow.exceptions.SchedulingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class SchedulingNotExistException extends SchedulingException {

    private final String detail;

    public SchedulingNotExistException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Schedule not exist");
        pd.setDetail(detail);

        return pd;
    }
}
