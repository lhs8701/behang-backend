package bh.bhback.global.common.jwt.repository;

import bh.bhback.global.common.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserKey(Long userKey);
    void deleteAllByUserKey(Long userKey);

    @Transactional
    void deleteByUserKey(Long userKey);
}
