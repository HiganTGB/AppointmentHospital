package appointmenthospital.thymeleafclient.model.entity;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {
    public final static int PERMISSIONS_STRING_LIMIT = 60;
    public final static int PERMISSION_LIMIT = PERMISSIONS_STRING_LIMIT * 8;
    private String name;
    private String description;
    private byte[] permissions=new byte[PERMISSIONS_STRING_LIMIT];
    private List<User> users;
    public Role(String name) {
        this.name=name;
    }

    public boolean isPermissionGranted(int permissionCode) {
        int x = permissionCode >>> 3, y = (~permissionCode) & 7;
        return ((permissions[x] >>> y) & 1) != 0;
    }
    public void setPermissionGranted(int permissionCode, boolean value) {
        int x = permissionCode >>> 3, y = (~permissionCode) & 7, z = 1 << y;
        if (value) permissions[x] |= (byte) z;
        else permissions[x] &= (byte) ~z;
    }

    public static List<String> permissionIdentitiesOf(byte[] permissionsString) {
        List<String> permissionIdentities = new ArrayList<>();
        for (int i = 0; i < PERMISSIONS_STRING_LIMIT; ++i) {
            if (permissionsString[i] == 0) continue;
            for (int j = 0; j < 8; ++j) {
                if ((((permissionsString[i] >> ((~j) & 7))) & 1) == 0) continue;
                permissionIdentities.add(Permission.getByIdentity(i << 3 | j));
            }
        }
        return permissionIdentities;
    }
    public static byte[] permissionsStringOf(List<String> identities) {
        if (identities == null) return null;
        byte[] permissions = new byte[PERMISSIONS_STRING_LIMIT];
        for (String id : identities) {
            int code = Permission.getByCode(id);
            permissions[code >>> 3] |= 1 << ((~code) & 7);
        }
        return permissions;
    }
}
