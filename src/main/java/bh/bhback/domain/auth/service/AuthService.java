package bh.bhback.domain.auth.service;


import bh.bhback.domain.auth.dto.UserSignupRequestDto;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.common.jwt.dto.TokenDto;
import bh.bhback.global.common.jwt.dto.TokenRequestDto;
import bh.bhback.global.common.jwt.entity.JwtExpiration;
import bh.bhback.global.error.advice.exception.CRefreshTokenException;
import bh.bhback.global.error.advice.exception.CRefreshTokenExpiredException;
import bh.bhback.global.error.advice.exception.CUserExistException;
import bh.bhback.global.redis.LogoutAccessToken;
import bh.bhback.global.redis.LogoutAccessTokenRedisRepository;
import bh.bhback.global.redis.RefreshToken2;
import bh.bhback.global.redis.RefreshTokenRedisRepository;
import bh.bhback.global.security.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserJpaRepository userJpaRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    @Transactional
    public Long socialSignup(UserSignupRequestDto userSignupRequestDto) {
        if (userJpaRepository
                .findBySocialIdAndProvider(userSignupRequestDto.toEntity().getSocialId(), userSignupRequestDto.getProvider())
                .isPresent()
        ) throw new CUserExistException();
        return userJpaRepository.save(userSignupRequestDto.toEntity()).getUserId();
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {

        String oldAccessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(oldAccessToken);
        User user = (User) authentication.getPrincipal();

        String oldRefreshToken = tokenRequestDto.getRefreshToken();
        RefreshToken2 oldRedisRefreshToken = refreshTokenRedisRepository.findById(user.getUserId()).orElseThrow(CRefreshTokenExpiredException::new);

        if (oldRefreshToken.equals(oldRedisRefreshToken.getRefreshToken())) {
            String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getRoles());
            if (jwtProvider.getExpiration(oldRefreshToken) < JwtExpiration.REISSUE_EXPIRATION_TIME.getValue()) {
                String refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getRoles());
                refreshTokenRedisRepository.save(new RefreshToken2(user.getUserId(), refreshToken));
                return TokenDto.builder()
                        .grantType("bearer")
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
            return TokenDto.builder()
                    .grantType("bearer")
                    .accessToken(accessToken)
                    .refreshToken(oldRefreshToken)
                    .build();
        }
        // 리프레시 토큰 불일치 에러
        else {
            throw new CRefreshTokenException();
        }
    }

    public void logout(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        User user = (User) authentication.getPrincipal();
        long remainMilliSeconds = jwtProvider.getExpiration(accessToken);

        refreshTokenRedisRepository.deleteById(user.getUserId());
        logoutAccessTokenRedisRepository.save(new LogoutAccessToken(accessToken, user.getUserId(), remainMilliSeconds));
    }

    public void withdrawal(String accessToken, User user) {
        long remainMilliSeconds = jwtProvider.getExpiration(accessToken);

        refreshTokenRedisRepository.deleteById(user.getUserId());
        logoutAccessTokenRedisRepository.save(new LogoutAccessToken(accessToken, user.getUserId(), remainMilliSeconds));
        userJpaRepository.deleteById(user.getUserId());
    }
}
