package bh.bhback.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(-1000,"해당 회원을 조회할 수 없습니다."),
    LOGIN_FAILED(-1001, "로그인 오류."),
    AUTHENTICATION_ENTRY_POINT_ERROR(-1002, "해당 리소스에 접근하기 위한 권한이 없습니다."), //정상적으로 Jwt이 제대로 오지 않은 경우
    ACCESS_DENIED(-1003,"해당 리소스에 접근할 수 없는 권한입니다."),
    SIGNUP_FAILED(-1004,"이미 가입된 이메일입니다."),
    COMMUNICATION_EXCEPTION(-1005, "소셜 인증 과정 중 오류가 발생했습니다."),
    USER_EXIST_EXCEPTION(-1006, "이미 가입된 계정입니다. 로그인을 해주세요"),
    REFRESH_TOKEN_INVALID(-1007, "리프레쉬 토큰이 잘못되었습니다."),
    AGREEMENT_EXCEPTION(-1008, "필수동의 항목에 대해 동의가 필요합니다."),
    POST_NOT_FOUND(-1009,"해당 게시물을 찾을 수 없습니다."),
    WRONG_FILE_TYPE_EXCEPTION(-1010, "파일이 없거나, 지원하지 않는 형식입니다."),
    EXPIRED_ACCESS_TOKEN(-1011, "액세스 토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN(-1012, "지원하지 않는 토큰입니다."),
    WRONG_TOKEN(-1013, "잘못된 토큰입니다."),
    REFRESH_TOKEN_EXPIRED(-1014, "리프레쉬 토큰이 만료되었습니다. 로그인을 다시 해주세요."),
    LOGOUT_ERROR(-1015, "이미 로그아웃된 회원입니다."),
    REPORT_DUPLICATED_ERROR(-1016, "이미 신고한 게시물입니다."),

    WRONG_APPROACH(-9998, "잘못된 접근입니다."),
    INTERNAL_SERVER_ERROR(-9999, "서버 에러입니다.");

    private int code;
    private String description;
}
