package bh.bhback.global.error.advice;

import bh.bhback.domain.auth.exception.CCommunicationException;
import bh.bhback.domain.auth.exception.CSocialAgreementException;
import bh.bhback.domain.common.ResponseService;
import bh.bhback.domain.image.exception.WrongFileTypeException;
import bh.bhback.domain.model.response.CommonResult;
import bh.bhback.global.error.ErrorCode;

import bh.bhback.global.error.advice.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;
    private final MessageSource messageSource;

    //default exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        log.info(String.valueOf(e));
        return responseService.getFailResult
                (ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getDescription());

    }

    //해당 user를 찾지 못했을 떄
    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    //로그인 실패시
    @ExceptionHandler(CLoginFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult usernameLoginFailedException(HttpServletRequest request, CLoginFailedException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult authenticationEntrypointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult
                (ErrorCode.ACCESS_DENIED.getCode(), ErrorCode.ACCESS_DENIED.getDescription());
    }

    /**
     * refresh token이 잘못되었을 경우
     */
    @ExceptionHandler(CRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
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
    @ExceptionHandler(WrongApproachException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult wrongApproachException(HttpServletRequest request, WrongApproachException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 게시물을 찾을 수 없는 경우
     */
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult postNotFoundException(HttpServletRequest request, PostNotFoundException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 파일이 없거나, 잘못된 파일 형식일 경우
     */
    @ExceptionHandler(WrongFileTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult wrongFileTypeException(HttpServletRequest request, WrongFileTypeException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

    /**
     * 리프레쉬 토큰이 만료되었을 경우
     */
    @ExceptionHandler(CRefreshTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult refreshTokenExpiredException(HttpServletRequest request, CRefreshTokenExpiredException e) {
        return responseService.getFailResult
                (e.getErrorCode().getCode(), e.getErrorCode().getDescription());
    }

}




