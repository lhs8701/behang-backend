package bh.bhback.domain.image.service;

import bh.bhback.domain.image.dto.ImageDto;
import bh.bhback.domain.image.entity.Image;
import bh.bhback.global.error.advice.exception.WrongFileTypeException;
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
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    private final ImageJpaRepository imageJpaRepository;

    public void uploadFile(MultipartFile file, String fileName) {
        // 이미지가 없거나, 잘못된 형식일 경우
        if (file.isEmpty() || !file.getContentType().startsWith("image")) {
            throw new WrongFileTypeException();
        }
//      String fileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);

        //전체 저장 경로
        String fileUrl = makeFileUrl(fileName);
        Path savePath = Paths.get(fileUrl);
        try {
            file.transferTo(savePath);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String makeFileName(String fileOriName) {
        int idx = fileOriName.lastIndexOf(".");
        String ext = fileOriName.substring(idx);
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ext;
        return fileName;
    }

    public String makeFileUrl(String fileName){
        // 날짜 폴더 생성
        String folderPath = makeFolder();
        String fileUrl = uploadPath + File.separator + folderPath + File.separator + fileName;
        return fileUrl;
    }

    public Image save(ImageDto imageDto){
        Image image = imageDto.toEntity();
        return imageJpaRepository.save(image);
    }

    public String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        // make folder ----
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}
