package bh.bhback.global.error.advice.exception;

import lombok.Getter;
import bh.bhback.global.error.ErrorCode;


@Getter
public class CRefreshTokenException extends RuntimeException {
    private final ErrorCode errorCode;
    public CRefreshTokenException(){
        super();
        errorCode = ErrorCode.REFRESH_TOKEN_INVALID;
    }
}
