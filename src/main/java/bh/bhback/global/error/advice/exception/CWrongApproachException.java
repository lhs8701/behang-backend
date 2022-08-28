package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CWrongApproachException extends RuntimeException{
    ErrorCode errorCode;

    public CWrongApproachException() {
        super();
        errorCode = ErrorCode.WRONG_APPROACH;
    }
}
