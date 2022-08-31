package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class CPlaceNotFoundException extends RuntimeException{
    ErrorCode errorCode;

    public CPlaceNotFoundException(){
        super();
        errorCode = ErrorCode.PLACE_NOT_FOUND;
    }
}
