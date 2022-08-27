package bh.bhback.domain.user.dto;

import bh.bhback.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileUpdateParam {
    private String nickName;
    private MultipartFile imageFile;
}
