package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.Getter;
@Getter
public class CUserExistException extends RuntimeException{
    private final ErrorCode errorCode;
    public CUserExistException(){
        super();
        errorCode = ErrorCode.USER_EXIST_EXCEPTION;
    }
}

