package appointmenthospital.scheduleservice.service;

import appointmenthospital.scheduleservice.controller.DoctorInfoClient;
import appointmenthospital.scheduleservice.controller.RoomInfoClient;
import appointmenthospital.scheduleservice.model.dto.DoctorDomain;
import appointmenthospital.scheduleservice.model.dto.RoomDTO;
import appointmenthospital.scheduleservice.model.dto.ScheduleRequest;
import appointmenthospital.scheduleservice.model.entity.Schedule;
import appointmenthospital.scheduleservice.repository.ScheduleRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
        private final ScheduleRepository scheduleRepository;
        @Autowired
        private DoctorInfoClient doctorInfoClient;
        @Autowired
        private RoomInfoClient roomInfoClient;
        public Schedule create(ScheduleRequest request)
        {
            try
            {
             DoctorDomain doctor= doctorInfoClient.getDomain(request.getDoctorID());
            RoomDTO roomDTO=roomInfoClient.get(request.getRoomID());
            if(!doctor.getMedicalSpecialtyIDs().contains(roomDTO.getMedicalSpecialtyId()))
            {
                throw new IllegalStateException("Wrong specialty");
            }
            if(scheduleRepository.existsByRoomIDAndIsDayAndDayOfWeek(roomDTO.getId(), request.getIsDay(), request.getDayOfWeek()))
            {
                throw new IllegalStateException("Wrong specialty");
            }
            if(scheduleRepository.existsByDoctorIDAndIsDayAndDayOfWeek(doctor.getId(), request.getIsDay(),request.getDayOfWeek()))
            {
                throw new IllegalStateException("Wrong specialty");
            }
            var schedule=Schedule.builder()
                    .roomID(roomDTO.getId())
                    .doctorID(doctor.getId())
                    .dayOfWeek(request.getDayOfWeek())
                    .isDay(request.getIsDay())
                    .build();
            return scheduleRepository.save(schedule);
            }catch (FeignException ex)
            {
                    throw new IllegalStateException(ex.getMessage());
            }
        }
}
