package appointmenthospital.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Doctor extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Degree degree;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @Column(nullable = true)
    private String urlAvatar;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "doctor")
    @Column
    private List<Doctor_Specialty> doctorSpecialties;
    public String getFullName()
    {
        return String.format("%s %s",this.user.getFirstName(),this.user.getLastName());
    }
}
