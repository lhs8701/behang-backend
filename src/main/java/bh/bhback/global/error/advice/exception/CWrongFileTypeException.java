package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CWrongFileTypeException extends RuntimeException {
    ErrorCode errorCode;
    public CWrongFileTypeException(){
        super();
        this.errorCode = ErrorCode.WRONG_FILE_TYPE_EXCEPTION;
    }
}
