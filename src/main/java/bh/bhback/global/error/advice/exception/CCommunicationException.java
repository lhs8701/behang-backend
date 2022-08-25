package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class CCommunicationException extends RuntimeException{
    private final ErrorCode errorCode;
    public CCommunicationException(){
        super();
        errorCode = ErrorCode.COMMUNICATION_EXCEPTION;
    }
}


