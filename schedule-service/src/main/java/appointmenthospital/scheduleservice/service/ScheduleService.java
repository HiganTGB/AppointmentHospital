package appointmenthospital.scheduleservice.service;

import appointmenthospital.scheduleservice.client.DoctorInfoClient;
import appointmenthospital.scheduleservice.client.RoomInfoClient;
import appointmenthospital.scheduleservice.log.CustomLogger;
import appointmenthospital.scheduleservice.model.domain.DoctorDomain;
import appointmenthospital.scheduleservice.model.domain.RoomDomain;
import appointmenthospital.scheduleservice.model.dto.*;
import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import appointmenthospital.scheduleservice.model.entity.QSchedule;
import appointmenthospital.scheduleservice.model.entity.Schedule;
import appointmenthospital.scheduleservice.repository.ScheduleRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        private final QSchedule schedule=QSchedule.schedule;
        public ScheduleDTO create(ScheduleRequest request)
        {
            try
            {
                DoctorDomain doctor= doctorInfoClient.getDomain(request.getDoctorID());
                RoomDomain roomDTO=roomInfoClient.getDomain(request.getRoomID());
            if(!doctor.getMedicalSpecialtyIDs().contains(roomDTO.getMedicalSpecialtyId()))
            {
                customLogger.log(doctor.toString(),roomDTO.toString());
                throw new IllegalStateException("Wrong specialty");
            }
            if(scheduleRepository.existsByRoomIDAndAtMorningAndDayOfWeek(roomDTO.getId(), request.getAtMorning(), request.getDayOfWeek()))
            {

                throw new IllegalStateException("Schedule exists by room with id: "+roomDTO.getId() );
            }
            if(scheduleRepository.existsByDoctorIDAndAtMorningAndDayOfWeek(doctor.getId(), request.getAtMorning(),request.getDayOfWeek()))
            {
                throw new IllegalStateException("Schedule exists by doctor with id: "+doctor.getId());
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
            RoomDomain roomDTO=roomInfoClient.getDomain(request.getRoomID());
            if(!doctor.getMedicalSpecialtyIDs().contains(roomDTO.getMedicalSpecialtyId()))
            {
                customLogger.log(doctor.toString(),roomDTO.toString());
                throw new IllegalStateException("Wrong specialty");
            }
            if(scheduleRepository.existsByRoomIDAndAtMorningAndDayOfWeek(roomDTO.getId(), request.getAtMorning(), request.getDayOfWeek()))
            {

                throw new IllegalStateException("Schedule exists by room with id: "+roomDTO.getId() );
            }
            if(scheduleRepository.existsByDoctorIDAndAtMorningAndDayOfWeek(doctor.getId(), request.getAtMorning(),request.getDayOfWeek()))
            {
                throw new IllegalStateException("Schedule exists by doctor with id: "+doctor.getId());
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
    public List<AvailableDateDTO> getAvailableDateBySpecialty(Long id)
    {
        List<RoomDomain> roomDTOs=roomInfoClient.getRooms(id);
        List<Long> roomIDs=roomDTOs.stream().map(RoomDomain::getId).toList();
        List<AvailableDateDTO> availableDateDTOS= new ArrayList<>();
        for(DayOfWeek d:DayOfWeek.values())
        {
                availableDateDTOS.add(new AvailableDateDTO(d.getValue(),scheduleRepository.existsByRoomIDInAndDayOfWeek(roomIDs,d)));
        }
        return availableDateDTOS;
    }
    public List<ScheduleDTO> getBySpecialtyAndDayOfWeek(Long specialty_id,DayOfWeek dayOfWeek)
    {
        List<RoomDomain> roomDTOs=roomInfoClient.getRooms(specialty_id);
        List<Long> roomIDs=roomDTOs.stream().map(RoomDomain::getId).toList();
        BooleanExpression bySpecialty=schedule.roomID.in(roomIDs);
        BooleanExpression byDayOfWeek=schedule.dayOfWeek.eq(dayOfWeek);
        List<ScheduleDTO> scheduleDTOS=new ArrayList<>();
        scheduleRepository.findAll(byDayOfWeek.and(bySpecialty)).forEach(x->scheduleDTOS.add(new ScheduleDTO(x)));
        return scheduleDTOS;
    }
    public ScheduleDTO getDomain(Long id)
    {
        return new ScheduleDTO(scheduleRepository.getReferenceById(id));
    }
}
