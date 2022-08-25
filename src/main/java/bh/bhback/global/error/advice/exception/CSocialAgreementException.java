package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CSocialAgreementException extends RuntimeException {
    private final ErrorCode errorCode;

    public CSocialAgreementException(){
        super();
        errorCode = ErrorCode.AGREEMENT_EXCEPTION;
    }
}
