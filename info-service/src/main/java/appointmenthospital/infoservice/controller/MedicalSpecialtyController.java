package appointmenthospital.infoservice.controller;

import appointmenthospital.infoservice.model.dto.DoctorDomain;
import appointmenthospital.infoservice.model.dto.MedicalSpecialtyDTO;
import appointmenthospital.infoservice.model.dto.RoomDTO;
import appointmenthospital.infoservice.service.MedicalSpecialtyService;
import appointmenthospital.infoservice.service.RoomService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specialties")
@RequiredArgsConstructor
@Tag(name = "Specialties API", description = "All about specialties, liên quan đến room và chuyên khoa bác sĩ")
public class MedicalSpecialtyController {
    private final MedicalSpecialtyService medicalSpecialtyService;
    private final RoomService roomService;
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MedicalSpecialtyDTO get(@PathVariable(name = "id") Long id)
    {
        return medicalSpecialtyService.get(id);
    }
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MedicalSpecialtyDTO update(@PathVariable Long id, @Valid @RequestBody MedicalSpecialtyDTO roomDTO)
    {
        return medicalSpecialtyService.update(roomDTO,id);
    }
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MedicalSpecialtyDTO create(@Valid @RequestBody MedicalSpecialtyDTO roomDTO)
    {
        return medicalSpecialtyService.create(roomDTO);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<MedicalSpecialtyDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
            @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return medicalSpecialtyService.getPage(keyword,pageable);
    }
    @PostMapping("/domain/{id}/add")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Hidden
    public Long add(@PathVariable Long id, @RequestBody DoctorDomain doctorDomain)
    {
        return medicalSpecialtyService.addDoctor(doctorDomain,id);
    }
    @PostMapping("/domain/{id}/remove")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Hidden
    public Boolean remove(@PathVariable Long id)
    {
        return medicalSpecialtyService.removeDoctor(id);
    }
    @GetMapping("/domain/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MedicalSpecialtyDTO getDomain(@PathVariable(name = "id") String id)
    {
        return medicalSpecialtyService.get(Long.parseLong(id));
    }

//    @GetMapping("/public/{id}/rooms")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    @Operation(summary = "Get Rooms", description = "Get all room by specialties",tags = {"Public API"})
//    public List<RoomDTO> getRooms(@PathVariable Long id)
//    {
//        return roomService.getAllByMedicalSpecialty(id);
//    }
    @GetMapping("/public/lists")
    @Operation(summary = "Specialties list", description = "Get specialties list",tags = {"Public API"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MedicalSpecialtyDTO> getAll()
    {
        return medicalSpecialtyService.getAll();
    }
    @GetMapping("/public/doctor/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Specialties Doctor", description = "Get specialties by doctor ID",tags = {"Public API"})
    public List<MedicalSpecialtyDTO> getMedicalByDoctorID(@PathVariable Long id)
    {
        return medicalSpecialtyService.getAllByDoctorID(id);
    }
}
