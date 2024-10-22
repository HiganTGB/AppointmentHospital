package appointmenthospital.infoservice.model.entity;

import lombok.Getter;

@Getter
public enum Degree {
    BS("Bác Sĩ"),
    CK1("Chuyên Khoa 1" ),
    CK2("Chuyên Khoa 2" ),
    TS("Tiến sĩ" ),
    ThS("Thạc sĩ"),
    PGS("Phó giáo sư"),
    GS("Giáo sư");
    Degree(String s) {
    }
}
