package bh.bhback.domain.auth.social.kakao.service;

import bh.bhback.domain.auth.basic.dto.UserSignupRequestDto;
import bh.bhback.domain.auth.basic.dto.SocialLoginRequestDto;
import bh.bhback.domain.auth.basic.dto.UserSocialSignupRequestDto;
import bh.bhback.domain.auth.basic.service.AuthService;
import bh.bhback.domain.auth.social.kakao.dto.KakaoProfile;
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
@Service
@Slf4j
public class KakaoSignService {

    private final JwtProvider jwtProvider;
    private final UserJpaRepository userJpaRepository;
    private final KakaoApiService kakaoApiService;
    private final AuthService authService;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;


    public TokenResponseDto loginByKakao(SocialLoginRequestDto socialLoginRequestDto) {

        KakaoProfile kakaoProfile = kakaoApiService.getKakaoProfile(socialLoginRequestDto.getAccessToken());
        if (kakaoProfile == null)
            throw new CUserNotFoundException();

        User user = userJpaRepository.findBySocialIdAndProvider(String.valueOf(kakaoProfile.getId()), "kakao")
                .orElseThrow(CUserNotFoundException::new);
        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getRoles());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getRoles());
        TokenResponseDto tokenResponseDto = jwtProvider.createTokenDto(accessToken, refreshToken);

        //redis
        refreshTokenRedisRepository.save(new RefreshToken(user.getUserId(), tokenResponseDto.getRefreshToken()));

        return tokenResponseDto;
    }

    public void signupByKakao(UserSocialSignupRequestDto socialSignupRequestDto) {

        // 카카오에게서 사용자 정보 요청
        KakaoProfile kakaoProfile =
                kakaoApiService.getKakaoProfile(socialSignupRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new CUserNotFoundException();

        authService.socialSignup(UserSignupRequestDto.builder()
                .socialId(String.valueOf(kakaoProfile.getId()))
                .nickName(kakaoProfile.getKakao_account().getProfile().getNickname())
                .profileImage(kakaoProfile.getKakao_account().getProfile().getProfile_image_url())
                .provider("kakao")
                .build());
    }
}
