package bh.bhback.global.security;


import bh.bhback.domain.auth.jwt.dto.TokenResponseDto;
import bh.bhback.domain.auth.jwt.entity.JwtExpiration;
import bh.bhback.global.error.ErrorCode;
import bh.bhback.global.error.advice.exception.CAuthenticationEntryPointException;
import bh.bhback.domain.auth.jwt.repository.LogoutAccessTokenRedisRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.lang.String;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${spring.jwt.secret}") //암호키는 중요하므로 따로 빼서 관리
    private String secretKey;
    private final UserDetailsService userDetailsService;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private String ROLES = "roles";

//    @PostConstruct
//    protected void init() { //암호화 알고리즘에 쓰일 secretKey를 등록함
//        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes(StandardCharsets.UTF_8));
//    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Jwt 생성 (access, refresh토큰을 각각 만들어서 tokenDto로 만든 후 반환)
    public TokenResponseDto createTokenDto(String accessToken, String refreshToken) { //토큰에 저장할 유저 pk와 권한 리스트를 매개변수로 받는다.
        return TokenResponseDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessToken(Long userPk, List<String> roles) {
        // user 구분을 위해 Claims에 User Pk값 넣어줌 (Claim 정보에는 토큰에 부가적으로 실어 보낼 정보를 담을 수 있다)
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk)); //claim의 subject 안에는 userPk값이 들어있음
        claims.put("roles", roles); //claim에 key-value 형태로 roles 속성 추가

        // 생성날짜, 만료날짜를 위한 Date
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) //access token, refresh token 시 추가된 부분
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JwtExpiration.ACCESS_TOKEN_EXPIRATION_TIME.getValue()))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userPk, List<String> roles) {
// user 구분을 위해 Claims에 User Pk값 넣어줌 (Claim 정보에는 토큰에 부가적으로 실어 보낼 정보를 담을 수 있다)
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk)); //claim의 subject 안에는 userPk값이 들어있음
        claims.put("roles", roles); //claim에 key-value 형태로 roles 속성 추가

        // 생성날짜, 만료날짜를 위한 Date
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) //access token, refresh token 시 추가된 부분
                .setExpiration(new Date(now.getTime() + JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME.getValue()))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }


    // Jwt 로 인증정보를 조회
    public Authentication getAuthentication(String token) {
        // Jwt 에서 claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보가 없음
        if (claims.get(ROLES) == null) { //권한 있는지 확인
            throw new CAuthenticationEntryPointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject()); //pk값을 가지고 loadUserByUsername()을 통해 유저 엔티티를 받는다.
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰 복호화해서 가져오기
    // 만료된 토큰이여도 refresh token을 검증 후 재발급할 수 있도록 claims를 반환해 준다
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey(secretKey)).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(getSigningKey(secretKey)).build().parseClaimsJws(token).getBody().getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }

    // HTTP Request 의 Header 에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // jwt 의 유효성 및 만료일자 확인
    // Jwts에서 제공하는 예외처리를 이용
    // jwt 의 유효성 및 만료일자 확인
    public boolean validationToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey(secretKey)).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
        }
        return false;
    }

    public boolean validationToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey(secretKey)).build().parseClaimsJws(token);
            if (logoutAccessTokenRedisRepository.existsById(token)) {
                log.error("이미 로그아웃된 회원입니다.");
                request.setAttribute("exception", ErrorCode.LOGOUT_ERROR.getCode());
                return false;
            }
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
            request.setAttribute("exception", ErrorCode.EXPIRED_ACCESS_TOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
            request.setAttribute("exception", ErrorCode.WRONG_TOKEN.getCode());
        }
        return false;
    }
}