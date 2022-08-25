package bh.bhback.global.config.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserKey(Long userKey);
    void deleteAllByUserKey(Long userKey);

    @Transactional
    void deleteByUserKey(Long userKey);
}
