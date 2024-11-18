package appointmenthospital.appointmentservice.service;

import appointmenthospital.appointmentservice.client.RoomInfoClient;
import appointmenthospital.appointmentservice.client.ScheduleInfoClient;
import appointmenthospital.appointmentservice.client.SpecialtyInfoClient;
import appointmenthospital.appointmentservice.model.dto.*;
import appointmenthospital.appointmentservice.model.entity.*;
import appointmenthospital.appointmentservice.repository.ExaminationRepository;
import appointmenthospital.appointmentservice.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

@Service

public class ExaminationService {
    @Value("${appointment.max-in-hour}")
    private int max;
    private final PatientRepository patientRepository;
    private final PatientService patientService;
    private final ExaminationRepository examinationRepository;
    private final ScheduleInfoClient scheduleInfoClient;
    private final RoomInfoClient roomInfoClient;
    private final SpecialtyInfoClient specialtyInfoClient;
    private final QExamination examination=QExamination.examination;
    @Autowired
    public ExaminationService(PatientRepository patientRepository, PatientService patientService, ExaminationRepository examinationRepository, ScheduleInfoClient scheduleInfoClient, RoomInfoClient roomInfoClient, SpecialtyInfoClient specialtyInfoClient) {
        this.patientRepository = patientRepository;
        this.patientService = patientService;
        this.examinationRepository = examinationRepository;
        this.scheduleInfoClient = scheduleInfoClient;
        this.roomInfoClient = roomInfoClient;
        this.specialtyInfoClient = specialtyInfoClient;
    }
    // Lấy lịch ngày đó
    public List<AppointmentCheckTimeDTO> getByScheduleID(Long scheduleID, LocalDate date)
    {
        List<AppointmentCheckTimeDTO> appointmentCheckTimeDTOS =new ArrayList<>();
        ScheduleDTO domain=new ScheduleDTO();
       boolean isMorning= domain.getAtMorning();
        for(TimeSlot time:TimeSlot.values())
        {
            if(isMorning== time.isMorning())
            {
                int count=examinationRepository.countAllByDateAndRoomIDAndTimeSlotAndStatusNotLike(date, domain.getRoomID(),time, AppointmentStatus.CANCELED);
                appointmentCheckTimeDTOS.add(new AppointmentCheckTimeDTO(time,count<max));
            }
        }
        return appointmentCheckTimeDTOS;
    }
    public void create(ExaminationRequest request, Principal connectedUser)
    {
        //Get user
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        ScheduleDTO scheduleDomain=scheduleInfoClient.get(request.getScheduleID());
        //Đếm tổng số lượng của slot đó
        int count=examinationRepository.countAllByDateAndRoomIDAndTimeSlotAndStatusNotLike(request.getDate(),scheduleDomain.getRoomID(),request.getTimeSlot(),AppointmentStatus.CANCELED);
        if(count>=max) // vượi giới hạn báo lỗi
        {
            throw new IllegalStateException("max count");
        }
        if(request.getDate().isEqual(LocalDate.now()))
        {
            throw new IllegalStateException("cannot make appointment today");
        }
        PatientProfile profile=patientRepository.getReferenceById(request.getId());
        RoomDomain roomDomain=roomInfoClient.getDomain(scheduleDomain.getRoomID());
        MedicalSpecialtyDTO medicalSpecialtyDomain=specialtyInfoClient.getDomain(roomDomain.getMedicalSpecialtyId());
        var examination= Examination.builder()
                .accountID(user.getId())
                .doctorId(scheduleDomain.getDoctorID())
                .roomID(scheduleDomain.getRoomID())
                .roomName(roomDomain.getName())
                .specialtyName(roomDomain.getMedicalSpecialtyName())
                .specialtyID(roomDomain.getMedicalSpecialtyId())
                .price(medicalSpecialtyDomain.getPrice())
                .date(request.getDate())
                .timeSlot(request.getTimeSlot())
                .status(AppointmentStatus.PENDING)
                .patientProfile(profile)
                .number(request.getTimeSlot().getOrder()*max+count+1)
                .build();
        examinationRepository.save(examination);
        //Trả về Thanh toán vnpay

        //
    }
    public Examination paid(Long id)
    {
        Examination examination=examinationRepository.getReferenceById(id);
        examination.setStatus(AppointmentStatus.PAID);
        return examinationRepository.save(examination);
    }
    public Examination cancel(Long id)
    {
        Examination examination=examinationRepository.getReferenceById(id);
        examination.setStatus(AppointmentStatus.CANCELED);
        return examinationRepository.save(examination);
    }
    public Boolean complete(Long id,String UUID)
    {
        Examination examination=examinationRepository.getReferenceById(id);
        examination.setStatus(AppointmentStatus.COMPLETED);
        if(UUID!=null)
        {
            PatientProfile profileNOUUID=examination.getPatientProfile();
            PatientProfile profileUUID=patientRepository.getReferenceById(UUID);
            examination.setPatientProfile(profileUUID);
            examinationRepository.save(examination);
            patientService.updateUUID(profileNOUUID.getId(),profileUUID.getId());
        }else
        {
            examinationRepository.save(examination);
        }
        return true;
    }
//    public List<Examination> getPage(String keyword,ExaminationFilter filter,Pageable pageable)
//    {
//
//
//    }
}
