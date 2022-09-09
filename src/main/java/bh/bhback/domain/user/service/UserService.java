package bh.bhback.domain.user.service;


import bh.bhback.domain.image.dto.ImageDto;
import bh.bhback.domain.image.service.ImageService;
import bh.bhback.domain.user.dto.UserProfileDto;
import bh.bhback.domain.user.dto.UserProfileUpdateParam;
import bh.bhback.domain.user.dto.UserResponseDto;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private UserJpaRepository userJpaRepository;
    private final ImageService imageService;

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
    public UserProfileDto updateMyProfile(UserProfileUpdateParam userProfileUpdateParam, User user) {
        MultipartFile file = userProfileUpdateParam.getImageFile();
        user.setNickName(userProfileUpdateParam.getNickName());
        imageService.deleteImage(user.getProfileImage());
        ImageDto imageDto = imageService.uploadProfileImage(file);
        user.setProfileImage(imageDto.getFileUrl());
        userJpaRepository.save(user);
        return new UserProfileDto(user);
    }
}