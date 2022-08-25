package bh.bhback.domain.auth.social.apple.service;

import bh.bhback.domain.auth.dto.UserSignupRequestDto;
import bh.bhback.domain.auth.service.AuthService;
import bh.bhback.domain.auth.social.apple.dto.AppleLoginRequestDto;
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
@Slf4j
@Service
public class AppleSignService {

    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenJpaRepo refreshTokenJpaRepo;
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    public TokenDto loginByApple(AppleLoginRequestDto appleLoginRequestDto) {
        User user = userJpaRepository.findBySocialIdAndProvider(appleLoginRequestDto.getSocialId(), "apple")
                .orElseThrow(CUserNotFoundException::new);
        TokenDto tokenDto = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());
        RefreshToken refreshToken = RefreshToken.builder()
                .userKey(user.getUserId())
                .token(tokenDto.getRefreshToken())
                .build();
        try{
            refreshTokenJpaRepo.deleteByUserKey(refreshToken.getUserKey());
        }catch (IllegalArgumentException e){
            log.info("회원가입 후 첫 로그인 시도");
        }
        refreshTokenJpaRepo.flush();
        refreshTokenJpaRepo.save(refreshToken);
        return tokenDto;
    }

    public Long signupByApple(AppleLoginRequestDto appleLoginRequestDto) {

        Long userId = authService.socialSignup(UserSignupRequestDto.builder()
                .socialId(appleLoginRequestDto.getSocialId())
                .nickName(appleLoginRequestDto.getNickName())
                .profileImage("https://www.studioapple.com/common/img/default_profile.png")
                .provider("apple")
                .build());

        return userId;
    }
}
