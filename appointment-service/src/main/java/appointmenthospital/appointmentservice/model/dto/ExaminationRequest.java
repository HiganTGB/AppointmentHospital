package appointmenthospital.appointmentservice.model.dto;

import appointmenthospital.appointmentservice.model.entity.Gender;
import appointmenthospital.appointmentservice.model.entity.TimeSlot;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class ExaminationRequest {
    @JsonProperty("schedule_id")
    private long scheduleID;
    @JsonProperty("time_slot")
    private TimeSlot timeSlot;
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("id")
    private String id;
}
