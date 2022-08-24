package bh.bhback.global.error.advice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import bh.bhback.global.error.ErrorCode;

@Slf4j
@Getter
@AllArgsConstructor
public class CSignupFailedException extends RuntimeException {
    private final ErrorCode errorCode;

    public CSignupFailedException() {
        super();
        errorCode = ErrorCode.SIGNUP_FAILED;
    }
}
