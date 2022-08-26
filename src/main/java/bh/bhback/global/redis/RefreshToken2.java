package bh.bhback.global.redis;

import bh.bhback.global.common.jwt.entity.JwtExpiration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken2 {

    @Id
    private Long id;

    private String refreshToken;

    @TimeToLive
    private Long expiration;

    public RefreshToken2(Long id, String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expiration = JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME.getValue() / 1000;
    }
}