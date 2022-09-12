package bh.bhback.domain.auth.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtExpiration {

    ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 60분", 1000L * 60 * 60),
    REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 14일", 1000L * 60 * 60 * 24 * 14),
//    Refresh 토큰 만료 2일 전에 reissue() 호출되면, refresh 토큰도 새로 발급됨
    REISSUE_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 5일", 1000L * 60 * 60 * 24 * 5);

//    ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 10초", 1000L * 10),
//    REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 20초", 1000L * 20),
//    REISSUE_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 3일", 1000L * 60 * 60 * 24 * 3);

    private final String description;
    private final Long value;
}