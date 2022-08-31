package bh.bhback.domain.history.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AreaCode {

    SEOUL(1, "Seoul"),
    INCHEON(2, "Incheon"),
    DAEJEON(3, "Daejeon"),
    DAEGU(4, "Daegu"),
    GWANGJU(5, "Gwangju"),
    BUSAN(6, "Busan"),
    ULSAN(7, "Ulsan"),
    SEJONG(8, "Sejong"),
    GYEONGGI(31, "Gyeonggi"),
    GANGWON(32, "Gangwon"),
    CHUNGBUK(33, "Chungbuk"),
    CHUNGNAM(34, "Chungnam"),
    GYUNGBUK(35, "Gyungbuk"),
    GYUNGNAM(36, "Gyungnam"),
    JEONBUK(37, "Jeonbuk"),
    JEONNAM(38, "Jeonnam"),
    JEJU(39, "Jeju"),
    WRONG(-1, "Wrong approach");

    public static AreaCode find(int code){
        return Arrays.stream(values())
                .filter(areaCode -> areaCode.code == code)
                .findAny()
                .orElse(WRONG);
    }

    private int code;
    private String name;
}
