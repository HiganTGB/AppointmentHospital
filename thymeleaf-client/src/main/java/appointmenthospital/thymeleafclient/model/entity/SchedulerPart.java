package appointmenthospital.thymeleafclient.model.entity;

import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchedulerPart {
    public LocalTime start;
    public LocalTime end;
}
