package bh.bhback.domain.auth.jwt.repository;

import bh.bhback.domain.auth.jwt.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {
}
