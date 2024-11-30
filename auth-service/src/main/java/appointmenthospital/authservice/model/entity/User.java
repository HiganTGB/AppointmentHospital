package appointmenthospital.authservice.model.entity;

import appointmenthospital.authservice.token.Token;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import java.sql.Timestamp;

import java.util.*;


@SuppressWarnings("unused")
@JsonAutoDetect
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false,name = "password")
    private String password;
    @Column(nullable = false,name = "fullName")
    private String fullName;
    @Column(nullable = true,name = "email",unique = true)
    private String email;
    @Column(nullable = false,name = "email_verified",columnDefinition = "bit(1) default 0")
    private Boolean emailVerified=false;
    @Column(nullable = false,name = "phone",unique = true)
    private String phone;
    @Column(nullable = false,name = "phone_verified",columnDefinition = "bit(1) default 0")
    private Boolean phoneVerified=false;
    @Column(nullable = false,name = "is_staff")
    private Boolean isStaff;
    @Column(nullable = false)
    private boolean isEnabled;
    @Column(nullable = true)
    private String image;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id",nullable = true)
    private Role role;

    @OneToOne(mappedBy = "user",targetEntity = Doctor.class,fetch = FetchType.LAZY)
    private Doctor doctor;
    @OneToOne(mappedBy = "user",targetEntity = Patient.class,fetch = FetchType.LAZY)
    private Patient patient;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
    @OneToOne(mappedBy = "user",targetEntity = PasswordResetToken.class)
    private PasswordResetToken passwordResetToken;


    @JsonProperty("username")
    @Override
    public String getUsername() {
        return phone;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    public List<String> getPermissions()
    {
        if(this.role==null) return Collections.emptyList();

        return Role.permissionIdentitiesOf(this.role.getPermissions());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getPermissions());
    }
    public String getRoleType()
    {
        return (getIsStaff())? "STAFF":"CUSTOMER";
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
