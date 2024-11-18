package appointmenthospital.appointmentservice.model.dto;

import appointmenthospital.appointmentservice.model.entity.TimeSlot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppointmentCheckTimeDTO {
    TimeSlot timeSlot;
    boolean available;
}
