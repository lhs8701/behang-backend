package bh.bhback.domain.test;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestService {

    public MyLocalDateTime showLocalDateTime(){
        MyLocalDateTime myLocalDateTime = new MyLocalDateTime(LocalDateTime.now());
        return myLocalDateTime;
    }
}
