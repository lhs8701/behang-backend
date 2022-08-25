package bh.bhback.domain.auth.social.kakao.service;

import bh.bhback.domain.auth.dto.UserSignupRequestDto;
import bh.bhback.domain.auth.dto.SocialLoginRequestDto;
import bh.bhback.domain.auth.dto.UserSocialSignupRequestDto;
import bh.bhback.domain.auth.service.AuthService;
import bh.bhback.domain.auth.social.kakao.dto.KakaoProfile;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.security.JwtProvider;
import bh.bhback.global.common.jwt.entity.RefreshToken;
import bh.bhback.global.common.jwt.repository.RefreshTokenJpaRepo;
import bh.bhback.global.common.jwt.dto.TokenDto;
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
    private final RefreshTokenJpaRepo refreshTokenJpaRepo;
    private final KakaoApiService kakaoApiService;
    private final AuthService authService;


    public TokenDto loginByKakao(SocialLoginRequestDto socialLoginRequestDto) {

        KakaoProfile kakaoProfile = kakaoApiService.getKakaoProfile(socialLoginRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new CUserNotFoundException();

        User user = userJpaRepository.findBySocialIdAndProvider(String.valueOf(kakaoProfile.getId()), "kakao")
                .orElseThrow(CUserNotFoundException::new);
        TokenDto tokenDto = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());
        RefreshToken refreshToken = RefreshToken.builder()
                .userKey(user.getUserId())
                .token(tokenDto.getRefreshToken())
                .build();
        try{
            refreshTokenJpaRepo.deleteByUserKey(refreshToken.getUserKey());
        }catch (IllegalArgumentException e){
            log.info("해당 칼럼이 없음");
        }
        refreshTokenJpaRepo.flush();
        refreshTokenJpaRepo.save(refreshToken);
        return tokenDto;
    }

    public Long signupByKakao(UserSocialSignupRequestDto socialSignupRequestDto) {

        // 카카오에게서 사용자 정보 요청
        KakaoProfile kakaoProfile =
                kakaoApiService.getKakaoProfile(socialSignupRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new CUserNotFoundException();

        Long userId = authService.socialSignup(UserSignupRequestDto.builder()
                .socialId(String.valueOf(kakaoProfile.getId()))
                .nickName(kakaoProfile.getKakao_account().getProfile().getNickname())
                .profileImage(kakaoProfile.getKakao_account().getProfile().getProfile_image_url())
                .provider("kakao")
                .build());

        return userId;
    }
}
