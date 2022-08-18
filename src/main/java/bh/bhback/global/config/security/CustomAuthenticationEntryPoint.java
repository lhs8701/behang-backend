package bh.bhback.global.config.security;

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
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.sendRedirect("/exception/entrypoint");
    }
}
