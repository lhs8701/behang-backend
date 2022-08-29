package bh.bhback.domain.auth.jwt.repository;

import bh.bhback.domain.auth.jwt.entity.AppleRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppleRefreshTokenJpaRepository extends JpaRepository<AppleRefreshToken, String> {

}
