package bh.bhback.global.config.security;


import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
// 메소드에서 UserDetails (인터페이스)를 반환하도록 정의
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String memberPk) throws UsernameNotFoundException {
        return userJpaRepository.findById(Long.parseLong(memberPk)).orElseThrow(CUserNotFoundException::new);
    }
}