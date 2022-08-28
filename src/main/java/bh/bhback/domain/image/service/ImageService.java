package bh.bhback.domain.image.service;

import bh.bhback.domain.image.dto.ImageDto;
import bh.bhback.domain.image.repository.ImageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    @Value("${upload.path.post}")
    private String UploadPathPost;
    @Value("${upload.path.profile}")
    private String UploadPathProfile;
    private final ImageJpaRepository imageJpaRepository;

    public ImageDto uploadImage(MultipartFile file, String uploadPath) {
//        이미지가 없거나, 잘못된 형식일 경우
//        if (file.isEmpty() || !file.getContentType().startsWith("image")) {
//            throw new WrongFileTypeException();
//        }
//      String fileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);

        String fileOriName = Optional.ofNullable(file.getOriginalFilename()).orElse("empty");
        String fileName = makeFileName(fileOriName);
        String fileUrl = makeFileUrl(fileName, uploadPath);
        Path savePath = Paths.get(fileUrl);

        try {
            file.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ImageDto(fileName, fileOriName, fileUrl);
    }

    public ImageDto uploadPostImage(MultipartFile file) {
        return uploadImage(file, UploadPathPost);
    }

    public ImageDto uploadProfileImage(MultipartFile file){
        return uploadImage(file, UploadPathProfile);
    }

    public String makeFileName(String fileOriName) {
        int idx = fileOriName.lastIndexOf(".");
        String ext = fileOriName.substring(idx);
        String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }

    public String makeFileUrl(String fileName, String uploadPath) {
        String folderPath = makeFolder(uploadPath);
        return uploadPath + File.separator + folderPath + File.separator + fileName;
    }

    // 날짜 폴더 생성
    public String makeFolder(String uploadPath) {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}
