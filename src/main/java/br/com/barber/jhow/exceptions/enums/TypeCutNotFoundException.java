package br.com.barber.jhow.exceptions.enums;

import br.com.barber.jhow.exceptions.EnumException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TypeCutNotFoundException extends EnumException {

    private String detail;

    public TypeCutNotFoundException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Type cut not exist");
        pd.setDetail(detail);
        return pd;
    }
}
