package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CLogoutException extends RuntimeException{
    private final ErrorCode errorCode;

    public CLogoutException(){
        super();
        errorCode = ErrorCode.LOGOUT_ERROR;
    }
}
