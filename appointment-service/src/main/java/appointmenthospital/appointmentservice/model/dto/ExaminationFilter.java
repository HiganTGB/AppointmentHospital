package appointmenthospital.appointmentservice.model.dto;

import appointmenthospital.appointmentservice.model.entity.AppointmentStatus;

import java.util.Date;
import java.util.List;

public class ExaminationFilter {
    List<Long> doctor_id;
    List<Long> medicalSpecialty_id;
    List<Long> room_id;
    List<AppointmentStatus> status;
    List<Date> start;
    List<Date> date;

}
