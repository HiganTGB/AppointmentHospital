package appointmenthospital.infoservice.service;

import appointmenthospital.infoservice.model.dto.RoomDTO;
import appointmenthospital.infoservice.model.dto.RoomDomain;
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
import java.util.List;

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
        return new RoomDTO(roomRepository.save(room));
    }
    public RoomDTO update(RoomDTO roomDTO,Long id)
    {
        Room room=getEntity(id);
        room.setName(roomDTO.getName());
        room.setDescription(roomDTO.getDescription());
        MedicalSpecialty medicalSpecialty = getMedical(roomDTO.getMedicalSpecialtyId());
        room.setMedicalSpecialty(medicalSpecialty);
        return new RoomDTO(roomRepository.save(room));
    }
    public List<RoomDTO> getAll()
    {
        return roomRepository.findAll().stream().map(RoomDTO::new).toList();
    }

    public List<RoomDomain> getAllByMedicalSpecialty(Long id)
    {

        return roomRepository.findAllByMedicalSpecialtyId(id).stream().map(RoomDomain::new).toList();
    }
    public RoomDomain getDomain(Long id)
    {

        return new RoomDomain(roomRepository.getReferenceById(id));
    }

    public Page<RoomDTO> getPage(String keyword,Pageable pageable)
    {
        BooleanExpression expression= room.name.contains(keyword).or(room.medicalSpecialty.name.contains(keyword));
        return (roomRepository.findAll(expression,pageable).map(RoomDTO::new));
    }
    public RoomDTO get(Long id)
    {
        return modelMapper.map(getEntity(id),RoomDTO.class);
    }
    private Room getEntity(Long id)
    {

            return roomRepository.getReferenceById(id);

    }
    private MedicalSpecialty getMedical(Long id)
    {

            return medicalSpecialtyRepository.getReferenceById(id);


    }

}
