package bh.bhback.domain.auth.social.apple.service;

import bh.bhback.domain.auth.basic.dto.UserSignupRequestDto;
import bh.bhback.domain.auth.basic.service.AuthService;
import bh.bhback.domain.auth.jwt.entity.AppleRefreshToken;
import bh.bhback.domain.auth.jwt.repository.AppleRefreshTokenJpaRepository;
import bh.bhback.domain.auth.social.apple.dto.*;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.domain.auth.jwt.entity.RefreshToken;
import bh.bhback.domain.auth.jwt.repository.RefreshTokenRedisRepository;
import bh.bhback.global.security.JwtProvider;
import bh.bhback.domain.auth.jwt.dto.TokenResponseDto;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AppleSignService {

    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final AppleRefreshTokenJpaRepository appleRefreshTokenJpaRepository;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final AppleApiService appleApiService;

    public TokenResponseDto loginByApple(AppleLoginRequestDto appleLoginRequestDto) {
        String requestRefreshToken = appleLoginRequestDto.getRefreshToken();
        RetAppleLoginOAuth retAppleLoginOAuth = appleApiService.validateRefreshToken(requestRefreshToken);
        if (retAppleLoginOAuth == null)
            throw new CUserNotFoundException();

        User user = appleRefreshTokenJpaRepository
                .findById(requestRefreshToken)
                .orElseThrow(CUserNotFoundException::new)
                .getUser();
        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getRoles());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getRoles());
        TokenResponseDto tokenResponseDto = jwtProvider.createTokenDto(accessToken, refreshToken);

        refreshTokenRedisRepository.save(new RefreshToken(user.getUserId(), tokenResponseDto.getRefreshToken()));

        return tokenResponseDto;
    }

    public AppleSignupResponseDto signupByApple(AppleSignupRequestDto appleSignupRequestDto) {
        RetAppleSignOAuth retAppleSignOAuth = appleApiService.getAppleTokenInfo(appleSignupRequestDto);

        Long userId = authService.socialSignup(UserSignupRequestDto.builder()
                .socialId(appleSignupRequestDto.getUserId())
                .nickName(appleSignupRequestDto.getNickName())
                .profileImage("/static/default_profile_image.png")
                .provider("apple")
                .build());

        AppleRefreshToken appleRefreshToken = AppleRefreshToken.builder()
                .refreshToken(retAppleSignOAuth.getRefresh_token())
                .user(userJpaRepository.findById(userId).orElseThrow(CUserNotFoundException::new))
                .build();

        appleRefreshTokenJpaRepository.save(appleRefreshToken);

        return new AppleSignupResponseDto(retAppleSignOAuth.getRefresh_token());
    }
}
