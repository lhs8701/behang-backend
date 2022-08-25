package bh.bhback.domain.auth.service;


import bh.bhback.domain.auth.dto.UserSignupRequestDto;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.common.jwt.dto.TokenDto;
import bh.bhback.global.common.jwt.dto.TokenRequestDto;
import bh.bhback.global.common.jwt.repository.RefreshTokenJpaRepo;
import bh.bhback.global.error.advice.exception.CRefreshTokenException;
import bh.bhback.global.error.advice.exception.CRefreshTokenExpiredException;
import bh.bhback.global.error.advice.exception.CUserExistException;
import bh.bhback.global.security.*;
import bh.bhback.global.common.jwt.entity.RefreshToken;
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
    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

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
        // 만료된 refresh token 에러
        if (!jwtProvider.validationToken(tokenRequestDto.getRefreshToken())) {
            throw new CRefreshTokenExpiredException();
        }

        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        User user = (User) authentication.getPrincipal();

        RefreshToken refreshToken = refreshTokenJpaRepo.findByUserKey(user.getUserId())
                .orElseThrow(CRefreshTokenException::new);


        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new CRefreshTokenException();
        }

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenDto newCreatedToken = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
//        refreshTokenJpaRepo.deleteAllByUserKey(refreshToken.getUserKey());
        try{
            refreshTokenJpaRepo.deleteByUserKey(refreshToken.getUserKey());
        }catch (IllegalArgumentException e){
            log.info("해당 칼럼이 없음");
        }
        refreshTokenJpaRepo.flush();
        refreshTokenJpaRepo.save(updateRefreshToken);
        return newCreatedToken;
    }
}
