package bh.bhback.domain.auth.social.apple.service;

import bh.bhback.domain.auth.dto.UserSignupRequestDto;
import bh.bhback.domain.auth.service.AuthService;
import bh.bhback.domain.auth.social.apple.dto.*;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.redis.RefreshToken2;
import bh.bhback.global.redis.RefreshTokenRedisRepository;
import bh.bhback.global.security.JwtProvider;
import bh.bhback.global.common.jwt.dto.TokenDto;
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
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final AppleApiService appleApiService;

    public TokenDto loginByApple(String appleRefreshToken) {
        RetAppleLoginOAuth retAppleLoginOAuth = appleApiService.validateRefreshToken(appleRefreshToken);
        if (retAppleLoginOAuth == null)
            throw new CUserNotFoundException();
        AppleProfile appleProfile = appleApiService.getAppleProfile(appleRefreshToken);
        User user = userJpaRepository.findBySocialIdAndProvider(String.valueOf(appleProfile.getId()), "apple")
                .orElseThrow(CUserNotFoundException::new);
        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getRoles());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getRoles());
        TokenDto tokenDto = jwtProvider.createTokenDto(accessToken, refreshToken);

        //redis
        refreshTokenRedisRepository.save(new RefreshToken2(user.getUserId(), tokenDto.getRefreshToken()));

        return tokenDto;
    }

    public String signupByApple(AppleSignupRequestDto appleSignupRequestDto) {
        RetAppleSignOAuth retAppleSignOAuth;
        retAppleSignOAuth = appleApiService.getAppleTokenInfo(appleSignupRequestDto);
        AppleProfile appleProfile = appleApiService.getAppleProfile(retAppleSignOAuth.getRefresh_token());
        authService.socialSignup(UserSignupRequestDto.builder()
                .socialId(String.valueOf(appleProfile.getId()))
                .nickName(appleProfile.getNickName())
                .profileImage(appleProfile.getProfile())
                .provider("apple")
                .build());
        return retAppleSignOAuth.getRefresh_token();
    }
}
