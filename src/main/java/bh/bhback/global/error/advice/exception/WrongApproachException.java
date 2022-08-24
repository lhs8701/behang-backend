package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WrongApproachException extends RuntimeException{
    ErrorCode errorCode;

    public WrongApproachException() {
        super();
        errorCode = ErrorCode.WRONG_APPROACH;
    }
}
