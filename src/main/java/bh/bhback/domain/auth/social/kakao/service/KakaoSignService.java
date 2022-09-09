package bh.bhback.domain.auth.social.kakao.service;

import bh.bhback.domain.auth.basic.dto.LogoutWithdrawalRequestDto;
import bh.bhback.domain.auth.basic.dto.SignupRequestDto;
import bh.bhback.domain.auth.social.kakao.dto.KakaoLoginRequestDto;
import bh.bhback.domain.auth.social.kakao.dto.KakaoSignupRequestDto;
import bh.bhback.domain.auth.basic.service.AuthService;
import bh.bhback.domain.auth.jwt.entity.LogoutAccessToken;
import bh.bhback.domain.auth.jwt.repository.LogoutAccessTokenRedisRepository;
import bh.bhback.domain.auth.social.kakao.dto.KakaoProfile;
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
@Service
@Slf4j
public class KakaoSignService {

    private final JwtProvider jwtProvider;
    private final UserJpaRepository userJpaRepository;
    private final KakaoApiService kakaoApiService;
    private final AuthService authService;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final PostService postService;

    public void logout(String accessToken, LogoutWithdrawalRequestDto logoutRequestDto) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        User user = (User) authentication.getPrincipal();
        long remainMilliSeconds = jwtProvider.getExpiration(accessToken);

        kakaoApiService.kakaoLogout(logoutRequestDto.getSocialAccessToken());
        refreshTokenRedisRepository.deleteById(user.getUserId());
        logoutAccessTokenRedisRepository.save(new LogoutAccessToken(accessToken, user.getUserId(), remainMilliSeconds));
    }

    public void withdrawal(String accessToken, LogoutWithdrawalRequestDto withdrawalRequestDto, User user) {
        long remainMilliSeconds = jwtProvider.getExpiration(accessToken);

        postService.deleteAllPost(user);

        kakaoApiService.kakaoUnlink(withdrawalRequestDto.getSocialAccessToken());
        refreshTokenRedisRepository.deleteById(user.getUserId());
        logoutAccessTokenRedisRepository.save(new LogoutAccessToken(accessToken, user.getUserId(), remainMilliSeconds));
        userJpaRepository.deleteById(user.getUserId());
    }

    public TokenResponseDto loginByKakao(KakaoLoginRequestDto kakaoLoginRequestDto) {

        KakaoProfile kakaoProfile = kakaoApiService.getKakaoProfile(kakaoLoginRequestDto.getAccessToken());
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

    public void signupByKakao(KakaoSignupRequestDto kakaoSignupRequestDto) {

        // 카카오에게서 사용자 정보 요청
        KakaoProfile kakaoProfile =
                kakaoApiService.getKakaoProfile(kakaoSignupRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new CUserNotFoundException();

        String image = kakaoProfile.getKakao_account().getProfile().getProfile_image_url();
        if (image == null)
            image = "images/static/default_profile_image.png";

        authService.socialSignup(SignupRequestDto.builder()
                .socialId(String.valueOf(kakaoProfile.getId()))
                .nickName(kakaoProfile.getKakao_account().getProfile().getNickname())
                .profileImage(image)
                .provider("kakao")
                .build());
    }
}
