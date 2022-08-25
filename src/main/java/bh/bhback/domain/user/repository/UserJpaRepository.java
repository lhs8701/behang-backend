package bh.bhback.domain.user.repository;

import bh.bhback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialIdAndProvider(String socialId, String provider);
}