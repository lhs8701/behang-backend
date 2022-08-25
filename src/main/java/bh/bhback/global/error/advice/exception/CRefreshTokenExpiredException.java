package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CRefreshTokenExpiredException extends RuntimeException{
    private final ErrorCode errorCode;
    public CRefreshTokenExpiredException(){
        super();
        errorCode = ErrorCode.REFRESH_TOKEN_EXPIRED;
    }
}
