package appointmenthospital.appointmentservice.model.dto;

import appointmenthospital.appointmentservice.model.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@SuppressWarnings("unused")
@JsonAutoDetect
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false,name = "password")
    private String password;
    @Column(nullable = false,name = "firstName")
    private String firstName;
    @Column(nullable = false,name = "lastName")
    private String lastName;
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
    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;



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
    public String getRoleType()
    {
        return (getIsStaff())? "STAFF":"CUSTOMER";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
