package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WrongFileTypeException extends RuntimeException {
    ErrorCode errorCode;
    public WrongFileTypeException(){
        super();
        this.errorCode = ErrorCode.WRONG_FILE_TYPE_EXCEPTION;
    }
}
