package bh.bhback.global.error.advice;

import bh.bhback.global.error.advice.exception.CCommunicationException;
import bh.bhback.global.error.advice.exception.CSocialAgreementException;
import bh.bhback.global.common.response.service.ResponseService;
import bh.bhback.global.error.advice.exception.CWrongFileTypeException;
import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.error.ErrorCode;

import bh.bhback.global.error.advice.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;

    //default exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        log.info(String.valueOf(e));

        String exception = String.valueOf(request.getAttribute("exception"));

        if (exception.equals(String.valueOf(ErrorCode.LOGOUT_ERROR.getCode()))) {
            return responseService.getFailResult
                    (ErrorCode.LOGOUT_ERROR.getCode(), ErrorCode.LOGOUT_ERROR.getDescription());
        }
        return responseService.getFailResult
                (ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getDescription());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult validationException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.info(String.valueOf(e));
        return responseService.getFailResult
                (ErrorCode.VALIDATION_ERROR.getCode(), ErrorCode.VALIDATION_ERROR.getDescription());

    }

    //-------------------------Custum Error---------------------------------------//

    //해당 user를 찾지 못했을 떄
    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    //로그인 실패시
    @ExceptionHandler(CLoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult usernameLoginFailedException(HttpServletRequest request, CLoginFailedException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult authenticationEntrypointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult accessDeniedException(HttpServletRequest request, CAccessDeniedException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * refresh token이 잘못되었을 경우
     */
    @ExceptionHandler(CRefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult refreshTokenException(HttpServletRequest request, CRefreshTokenException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /***
     * Social 인증 과정에서 문제 발생하는 에러
     */
    @ExceptionHandler(CCommunicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult communicationException(HttpServletRequest request, CCommunicationException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /***
     * 기 가입자 에러
     */
    @ExceptionHandler(CUserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected CommonResult existUserException(HttpServletRequest request, CUserExistException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /***
     * 소셜 로그인 시 필수 동의항목 미동의시 에러
     */
    @ExceptionHandler(CSocialAgreementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult socialAgreementException(HttpServletRequest request, CSocialAgreementException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 잘못된 접근
     */
    @ExceptionHandler(CWrongApproachException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult wrongApproachException(HttpServletRequest request, CWrongApproachException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 게시물을 찾을 수 없는 경우
     */
    @ExceptionHandler(CPostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult postNotFoundException(HttpServletRequest request, CPostNotFoundException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 파일이 없거나, 잘못된 파일 형식일 경우
     */
    @ExceptionHandler(CWrongFileTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult wrongFileTypeException(HttpServletRequest request, CWrongFileTypeException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 리프레쉬 토큰이 만료되었을 경우
     */
    @ExceptionHandler(CRefreshTokenExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult refreshTokenExpiredException(HttpServletRequest request, CRefreshTokenExpiredException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 로그아웃된 회원의 토큰으로 접근
     */
    @ExceptionHandler(CLogoutException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult logoutException(HttpServletRequest request, CLogoutException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 중복 신고할 경우
     */
    @ExceptionHandler(CReportDuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult reportDuplicatedException(HttpServletRequest request, CReportDuplicatedException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 등록되지 않은 장소일 경우
     */
    @ExceptionHandler(CPlaceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult placeNotFoundException(HttpServletRequest request, CPlaceNotFoundException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

}




