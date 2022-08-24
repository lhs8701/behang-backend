package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public PostNotFoundException(){
        super();
        errorCode = ErrorCode.POST_NOT_FOUND;
    }
}
