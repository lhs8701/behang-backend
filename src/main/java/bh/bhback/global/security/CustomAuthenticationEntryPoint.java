package bh.bhback.global.security;

import bh.bhback.global.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//스프링 시큐리티는 Servlet Dispatcher 앞단에 존재한다. 따라서 스프링이 제어할 수 없는 영역에서 발생하는 예외를 처리하기 위해선 스프링 시큐리티가 제공하는 AuthenticationEntryPoiint를 상속받아서 재정의
//예외가 발생한 경우 "/exception/entrypoint"으로 리다이렉트
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        String exception = String.valueOf(request.getAttribute("exception"));

        //알수없는 오류
        if (exception == null) {
            setResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
        }
        //잘못된 타입의 토큰인 경우
        else if (exception.equals(String.valueOf(ErrorCode.WRONG_TOKEN.getCode()))) {
            setResponse(response, ErrorCode.WRONG_TOKEN);
        }
        //토큰 만료된 경우
        else if (exception.equals(String.valueOf(ErrorCode.EXPIRED_ACCESS_TOKEN.getCode()))) {
            setResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
        //지원되지 않는 토큰인 경우
        else if (exception.equals(String.valueOf(ErrorCode.UNSUPPORTED_TOKEN.getCode()))) {
            setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
        }
        else {
            response.sendRedirect("/exception/entrypoint");
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("success", false);
        responseJson.put("msg", errorCode.getDescription());
        responseJson.put("code", errorCode.getCode());

        response.getWriter().print(responseJson);
    }
}
