package br.com.barber.jhow.exceptions.scheduling;

import br.com.barber.jhow.exceptions.SchedulingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class SchedulingHourConflictException extends SchedulingException {

    private String detail;

    public SchedulingHourConflictException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("hour already exists");
        pd.setDetail(detail);

        return pd;
    }
}
