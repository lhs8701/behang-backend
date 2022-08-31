package bh.bhback.global.error.advice.exception;

import bh.bhback.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class CReportDuplicatedException extends RuntimeException{
    ErrorCode errorCode;

    public CReportDuplicatedException(){
        super();
        this.errorCode = ErrorCode.REPORT_DUPLICATED_ERROR;
    }
}
