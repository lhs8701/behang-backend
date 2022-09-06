package bh.bhback.domain.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@Slf4j
public class TestService {

    public MyLocalDateTime showLocalDateTime() {
        MyLocalDateTime myLocalDateTime = new MyLocalDateTime(LocalDateTime.now());
        return myLocalDateTime;
    }

}
