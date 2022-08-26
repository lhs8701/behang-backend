package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
public class CAccessDeniedException extends RuntimeException{
    private final ErrorCode errorCode;

    public CAccessDeniedException() {
        super();
        errorCode = ErrorCode.ACCESS_DENIED;
    }
}
