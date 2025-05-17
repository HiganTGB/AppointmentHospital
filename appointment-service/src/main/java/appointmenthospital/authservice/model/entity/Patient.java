package appointmenthospital.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Patient extends BaseEntity{
    @OneToMany(mappedBy = "patient",targetEntity = Profile.class)
    private List<Profile> profiles;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
}
