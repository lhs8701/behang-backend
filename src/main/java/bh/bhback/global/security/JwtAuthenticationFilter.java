package bh.bhback.global.security;

import bh.bhback.global.error.advice.exception.CLogoutException;
import bh.bhback.global.redis.LogoutAccessTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//Jwt이 유효한 토큰인지 인증하기 위한 Filter
//필터를 Security 설정 시 UsernamePasswordAuthentication 앞에 세팅해서 로그인폼으로 반환하기 전에 인증 여부를 Json으로 반환

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;


    // request 로 들어오는 Jwt 의 유효성을 검증 - JwtProvider.validationToken()을 필터로서 FilterChain 에 추가
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        // request 에서 token 을 취한다.
        String accessToken = jwtProvider.resolveToken((HttpServletRequest) request);

        // 검증
        log.info("[Verifying token]");
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        if (accessToken != null && jwtProvider.validationToken(accessToken, (HttpServletRequest) request)) {
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //다음 차례 필터 클래스 객체의 doFilter() 메소드를 호출시키는 기능
        filterChain.doFilter(request, response);
    }
}