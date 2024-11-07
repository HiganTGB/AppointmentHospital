package appointmenthospital.authservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Role extends BaseEntity {
    @Transient
    public final static int PERMISSIONS_STRING_LIMIT = 60;
    @Transient
    public final static int PERMISSION_LIMIT = PERMISSIONS_STRING_LIMIT * 8;


    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "description",nullable = true)
    private String description;
    @Column(columnDefinition = "binary(60)")
    private byte[] permissionsString=new byte[PERMISSIONS_STRING_LIMIT];

    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    public Role(String name) {
        this.name=name;
    }

    public boolean isPermissionGranted(int permissionCode) {
        int x = permissionCode >>> 3, y = (~permissionCode) & 7;
        return ((permissionsString[x] >>> y) & 1) != 0;
    }
    public void setPermissionGranted(int permissionCode, boolean value) {
        int x = permissionCode >>> 3, y = (~permissionCode) & 7, z = 1 << y;
        if (value) permissionsString[x] |= (byte) z;
        else permissionsString[x] &= (byte) ~z;
    }
    @Override
    public String toString() {
        return this.name;
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
