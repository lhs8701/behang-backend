package bh.bhback.domain.auth.social.apple.service;

import bh.bhback.domain.auth.basic.dto.LogoutWithdrawalRequestDto;
import bh.bhback.domain.auth.basic.dto.SignupRequestDto;
import bh.bhback.domain.auth.basic.service.AuthService;
import bh.bhback.domain.auth.jwt.entity.LogoutAccessToken;
import bh.bhback.domain.auth.jwt.repository.LogoutAccessTokenRedisRepository;
import bh.bhback.domain.auth.social.apple.dto.*;
import bh.bhback.domain.post.service.PostService;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.domain.auth.jwt.entity.RefreshToken;
import bh.bhback.domain.auth.jwt.repository.RefreshTokenRedisRepository;
import bh.bhback.global.security.JwtProvider;
import bh.bhback.domain.auth.jwt.dto.TokenResponseDto;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AppleSignService {

    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final PostService postService;

    public TokenResponseDto loginByApple(AppleLoginRequestDto appleLoginRequestDto) {
        User user = userJpaRepository.findBySocialIdAndProvider(appleLoginRequestDto.getSocialId(), "apple")
                .orElseThrow(CUserNotFoundException::new);
        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getRoles());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getRoles());
        TokenResponseDto tokenDto = jwtProvider.createTokenDto(accessToken, refreshToken);
        refreshTokenRedisRepository.save(new RefreshToken(user.getUserId(), tokenDto.getRefreshToken()));

        return tokenDto;
    }

    public Long signupByApple(AppleLoginRequestDto appleLoginRequestDto) {

        return authService.socialSignup(SignupRequestDto.builder()
                .socialId(appleLoginRequestDto.getSocialId())
                .nickName(appleLoginRequestDto.getNickName())
                .profileImage("images/static/default_profile_image.png")
                .provider("apple")
                .build());
    }

    public void withdrawal(String accessToken, User user) {
        long remainMilliSeconds = jwtProvider.getExpiration(accessToken);
        postService.deleteAllPost(user);

        refreshTokenRedisRepository.deleteById(user.getUserId());
        logoutAccessTokenRedisRepository.save(new LogoutAccessToken(accessToken, user.getUserId(), remainMilliSeconds));
        userJpaRepository.deleteById(user.getUserId());
    }

    public void logout(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        User user = (User) authentication.getPrincipal();
        long remainMilliSeconds = jwtProvider.getExpiration(accessToken);

        refreshTokenRedisRepository.deleteById(user.getUserId());
        logoutAccessTokenRedisRepository.save(new LogoutAccessToken(accessToken, user.getUserId(), remainMilliSeconds));
    }
}
