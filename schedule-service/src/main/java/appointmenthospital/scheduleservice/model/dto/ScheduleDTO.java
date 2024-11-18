package appointmenthospital.scheduleservice.model.dto;

import appointmenthospital.scheduleservice.model.domain.DoctorDomain;
import appointmenthospital.scheduleservice.model.domain.RoomDomain;
import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import appointmenthospital.scheduleservice.model.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class ScheduleDTO {
    private Long id;

    @JsonProperty("doctor_id")
    private Long doctorID;
    @JsonProperty("doctor_name")
    private String doctorName;

    @JsonProperty("room_id")
    private Long roomID;

    @JsonProperty("at_Morning")
    private Boolean atMorning;
    private DayOfWeek dayOfWeek;
    public ScheduleDTO(Schedule x, DoctorDomain domain) {
        this.id=x.getId();
        this.doctorID=x.getDoctorID();
        if(Objects.equals(x.getDoctorID(), domain.getId()))
        {
            this.doctorName=domain.getFullName();
        }
        this.atMorning=x.getAtMorning();
        this.dayOfWeek=x.getDayOfWeek();
        this.roomID= x.getRoomID();
    }
    public ScheduleDTO(Schedule x) {
        this.id=x.getId();
        this.doctorID=x.getDoctorID();
        this.atMorning=x.getAtMorning();
        this.dayOfWeek=x.getDayOfWeek();
        this.roomID= x.getRoomID();
    }
}
