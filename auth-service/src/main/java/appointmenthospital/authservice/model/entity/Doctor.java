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
public class Doctor extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Degree degree;
    @Column(nullable = false)
    private boolean gender;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @Column(nullable = true)
    private String urlAvatar;
    @OneToMany(fetch = FetchType.EAGER)
    @Column
    private List<Doctor_Specialty> doctorSpecialties;
}
