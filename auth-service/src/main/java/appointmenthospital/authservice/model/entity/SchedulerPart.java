package appointmenthospital.authservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalTime;
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class SchedulerPart extends BaseEntity {
    public LocalTime start;
    public LocalTime end;
}
