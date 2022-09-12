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
    @Value("${upload.root}")
    private String root;

    private final ImageJpaRepository imageJpaRepository;

    public ImageDto uploadImage(MultipartFile file, String uploadPath) {
//        이미지가 없거나, 잘못된 형식일 경우
//        if (file.isEmpty() || !file.getContentType().startsWith("image")) {
//            throw new WrongFileTypeException();
//        }
//      String fileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);

        String fileOriName = Optional.ofNullable(file.getOriginalFilename()).orElse("image");
        String fileName = makeFileName(fileOriName);
        String fileUrl = makeFileUrl(fileName, uploadPath);
        if (root == null)
            root = "";
        String transferUrl = root + fileUrl;
        Path savePath = Paths.get(transferUrl);

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
        //폴더 생성(folderPath) -> root + uploadPath + 날짜폴더
        //DB 저장값(fileUrl-리턴) -> uploadPath + 날짜폴더 + fileName
        String str_date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        if (root == null)
            root = "";
        String str_folder = root + uploadPath + str_date;
        String str_url = uploadPath + str_date + "/" + fileName;
        String folderPath = str_folder.replace("/", File.separator);

        File uploadPathFolder = new File(folderPath);
        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }

        return str_url.replace("/", File.separator);
    }

    public boolean deleteImage(String fileUrl){
        if (root == null)
            root = "";
        File file = new File(root + fileUrl);

        if (file.exists()) {
            if (file.delete()) {
                log.info("파일삭제 성공");
                return true;
            } else {
                log.info("파일삭제 실패");
                return false;
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
            return false;
        }
    }
}
