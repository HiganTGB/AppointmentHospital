package appointmenthospital.scheduleservice.service;

import appointmenthospital.scheduleservice.client.DoctorInfoClient;
import appointmenthospital.scheduleservice.client.RoomInfoClient;
import appointmenthospital.scheduleservice.log.CustomLogger;
import appointmenthospital.scheduleservice.log.LogDTO;
import appointmenthospital.scheduleservice.model.dto.DoctorDomain;
import appointmenthospital.scheduleservice.model.dto.RoomDTO;
import appointmenthospital.scheduleservice.model.dto.ScheduleDTO;
import appointmenthospital.scheduleservice.model.dto.ScheduleRequest;
import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import appointmenthospital.scheduleservice.model.entity.Schedule;
import appointmenthospital.scheduleservice.repository.ScheduleRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
        private final ScheduleRepository scheduleRepository;
        @Autowired
        private DoctorInfoClient doctorInfoClient;
        @Autowired
        private RoomInfoClient roomInfoClient;
        private final CustomLogger customLogger;
        public ScheduleDTO create(ScheduleRequest request)
        {
            try
            {
                DoctorDomain doctor= doctorInfoClient.getDomain(request.getDoctorID());
                RoomDTO roomDTO=roomInfoClient.get(request.getRoomID());
            if(!doctor.getMedicalSpecialtyIDs().contains(roomDTO.getMedicalSpecialtyId()))
            {
                customLogger.log(doctor.toString(),roomDTO.toString());
                throw new IllegalStateException("Wrong specialty 1");
            }
            if(scheduleRepository.existsByRoomIDAndAtMorningAndDayOfWeek(roomDTO.getId(), request.getAtMorning(), request.getDayOfWeek()))
            {
                throw new IllegalStateException("Wrong specialty 2");
            }
            if(scheduleRepository.existsByDoctorIDAndAtMorningAndDayOfWeek(doctor.getId(), request.getAtMorning(),request.getDayOfWeek()))
            {
                throw new IllegalStateException("Wrong specialty 3");
            }
            var schedule=Schedule.builder()
                    .roomID(roomDTO.getId())
                    .doctorID(doctor.getId())
                    .dayOfWeek(request.getDayOfWeek())
                    .atMorning(request.getAtMorning())
                    .build();
            return new ScheduleDTO(scheduleRepository.save(schedule),doctor) ;
            }catch (FeignException ex)
            {
                    throw new IllegalStateException(ex.getMessage());
            }
        }
    public ScheduleDTO update(ScheduleRequest request,Long id)
    {
        Schedule schedule= scheduleRepository.getReferenceById(id);
        try
        {
            DoctorDomain doctor= doctorInfoClient.getDomain(request.getDoctorID());
            RoomDTO roomDTO=roomInfoClient.get(request.getRoomID());
            if(!doctor.getMedicalSpecialtyIDs().contains(roomDTO.getMedicalSpecialtyId()))
            {
                throw new IllegalStateException("Wrong specialty");
            }
            if(scheduleRepository.existsByRoomIDAndAtMorningAndDayOfWeek(roomDTO.getId(), request.getAtMorning(), request.getDayOfWeek()))
            {
                throw new IllegalStateException("Wrong specialty");
            }
            if(scheduleRepository.existsByDoctorIDAndAtMorningAndDayOfWeek(doctor.getId(), request.getAtMorning(),request.getDayOfWeek()))
            {
                throw new IllegalStateException("Wrong specialty");
            }
            schedule.setDoctorID(doctor.getId());
            schedule.setAtMorning(request.getAtMorning());
            schedule.setRoomID(roomDTO.getId());
            schedule.setDayOfWeek(request.getDayOfWeek());
            return new ScheduleDTO(scheduleRepository.save(schedule),doctor) ;
        }catch (FeignException ex)
        {
            throw new IllegalStateException(ex.getMessage());
        }
    }
    public boolean delete(Long id)
    {
        scheduleRepository.deleteById(id);
        return true;
    }
    public List<ScheduleDTO> getByRoom(Long id)
    {
        List<ScheduleDTO> scheduleDTOS= new ArrayList<>();
        boolean morning=true;
            for(DayOfWeek d:DayOfWeek.values())
            {
                for (int i = 0; i < 2; i++)
                {
                    Schedule schedule=scheduleRepository.findFirstByRoomIDAndDayOfWeekAndAtMorning(id,d,morning);
                    if(schedule == null)
                    {
                        scheduleDTOS.add(null);
                    }else
                    {
                        DoctorDomain doctor= doctorInfoClient.getDomain(schedule.getDoctorID());
                        scheduleDTOS.add(new ScheduleDTO(schedule,doctor));
                    }
                    morning=!morning;
                }
            }
            return scheduleDTOS;
    }
}
