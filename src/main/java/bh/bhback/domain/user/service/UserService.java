package bh.bhback.domain.user.service;


import bh.bhback.domain.user.dto.UserProfileDto;
import bh.bhback.domain.user.dto.UserResponseDto;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private UserJpaRepository userJpaRepository;

//    @Transactional
//    public Long save(UserRequestDto userDto) {
//        User saved = userJpaRepo.save(userDto.toEntity());
//        return saved.getUserId();
//    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUser() {
        return userJpaRepository.findAll()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Transactional
    public UserProfileDto getUserProfile(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
        return new UserProfileDto(user);
    }

    @Transactional
    public UserProfileDto updateUserProfile(Long userId, UserProfileDto updateProfileDto) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
        user.setProfileImage(updateProfileDto.getProfileImage());
        user.setNickName(updateProfileDto.getNickName());
        return updateProfileDto;
    }
}