package bh.bhback.domain.auth.jwt.repository;

import bh.bhback.domain.auth.jwt.entity.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
