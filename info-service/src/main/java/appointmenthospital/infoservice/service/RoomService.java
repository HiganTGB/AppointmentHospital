package appointmenthospital.infoservice.service;

import appointmenthospital.infoservice.exc.ItemNotFoundException;
import appointmenthospital.infoservice.model.dto.RoomDTO;
import appointmenthospital.infoservice.model.entity.MedicalSpecialty;
import appointmenthospital.infoservice.model.entity.QRoom;
import appointmenthospital.infoservice.model.entity.Room;
import appointmenthospital.infoservice.repository.MedicalSpecialtyRepository;
import appointmenthospital.infoservice.repository.RoomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final Type pageType = new TypeToken<Page<RoomDTO>>(){}.getType();
    private final RoomRepository roomRepository;
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final ModelMapper modelMapper;
    private final QRoom room=QRoom.room;
    public RoomDTO create(RoomDTO roomDTO)
    {
        if(roomDTO.getMedicalSpecialtyId()==0)
        {
            var room=Room.builder().name(roomDTO.getName()).description(roomDTO.getDescription()).medicalSpecialty(null).build();
            return modelMapper.map(roomRepository.save(room),RoomDTO.class);
        }
        MedicalSpecialty medicalSpecialty = getMedical(roomDTO.getMedicalSpecialtyId());
        var room=Room.builder().name(roomDTO.getName()).description(roomDTO.getDescription()).medicalSpecialty(medicalSpecialty).build();
        return modelMapper.map(roomRepository.save(room),RoomDTO.class);
    }
    public RoomDTO update(RoomDTO roomDTO,Long id)
    {
        Room room=getEntity(id);
        room.setName(roomDTO.getName());
        room.setDescription(roomDTO.getDescription());
        MedicalSpecialty medicalSpecialty = getMedical(roomDTO.getMedicalSpecialtyId());
        room.setMedicalSpecialty(medicalSpecialty);
        return modelMapper.map(roomRepository.save(room),RoomDTO.class);
    }
    public Page<RoomDTO> getPage(String keyword,Pageable pageable)
    {
        BooleanExpression expression= room.name.contains(keyword).or(room.medicalSpecialty.name.contains(keyword));
        return modelMapper.map(roomRepository.findAll(expression,pageable),pageType);
    }
    public RoomDTO get(Long id)
    {
        return modelMapper.map(getEntity(id),RoomDTO.class);
    }
    private Room getEntity(Long id)
    {
        try
        {
            return roomRepository.getReferenceById(id);
        }catch (EmptyResultDataAccessException e)
        {
            throw new ItemNotFoundException("Medical Specialty with " +id + " not Found");
        }
    }
    private MedicalSpecialty getMedical(Long id)
    {
        try
        {
            return medicalSpecialtyRepository.getReferenceById(id);
        }catch (EmptyResultDataAccessException e)
        {
            throw new ItemNotFoundException("Medical Specialty with " +id + " not Found");
        }

    }

}
