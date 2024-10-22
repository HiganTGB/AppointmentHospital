package appointmenthospital.authservice.model.entity;

import lombok.Getter;

@Getter
public enum Permission{

    CREATE_FOO(1,"Create Foo" ),
    READ_FOO(2,"Read Foo" ),
    UPDATE_FOO(3,"Update Foo" ),
    DELETE_FOO(4,"Delete Foo" ),
    OPTIONAL_FOO(5,"Optional Foo");
    private final int code;

    Permission(int code, String description) {
        this.code = code;
    }
    Permission(int code)
    {
        this.code=code;
    }

    public static String getByIdentity(int code) {
        for (Permission permission : Permission.values()) {
            if (permission.code == code) {
                return permission.name();
            }
        }
        return null;
    }

    public static int getByCode (String identity) {
        for (Permission permission : Permission.values()) {
            if (permission.name().equals(identity)) {
                return permission.code;
            }
        }
        return 0;
    }

}