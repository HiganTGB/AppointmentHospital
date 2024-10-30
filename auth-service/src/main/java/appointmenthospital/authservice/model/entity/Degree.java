package appointmenthospital.authservice.model.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Degree {
    BS("Bác sĩ"),
    CK1("Chuyên khoa 1" ),
    CK2("Chuyên khoa 2" ),
    TS("Tiến sĩ" ),
    ThS("Thạc sĩ"),
    PGS("Phó giáo sư"),
    GS("Giáo sư");
    private static final Map<String, Degree> BY_NAME = new HashMap<>();

    static {
        for (Degree degree : values()) {
            BY_NAME.put(degree.getName(), degree);
        }
    }

    private final String name;

    Degree(String name) {
        this.name = name;
    }

    public static Degree fromString(String name) {
        return BY_NAME.get(name);
    }
}
