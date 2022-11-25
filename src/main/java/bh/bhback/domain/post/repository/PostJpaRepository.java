package bh.bhback.domain.post.repository;

import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    Optional<List<Post>> findAllByUser(User user);
    Optional<List<Post>> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Optional<List<Post>> findAllByUserOrderByCreatedDateDesc(User user, Pageable pageable);

}
