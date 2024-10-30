package appointmenthospital.infoservice.controller;

import appointmenthospital.infoservice.model.dto.DoctorDTO;
import appointmenthospital.infoservice.model.dto.MedicalSpecialtyDTO;
import appointmenthospital.infoservice.model.dto.RoomDTO;
import appointmenthospital.infoservice.model.entity.Specialty_Doctor;
import appointmenthospital.infoservice.service.MedicalSpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/specialty")
@RequiredArgsConstructor
public class MedicalSpecialtyController {
    private final MedicalSpecialtyService medicalSpecialtyService;
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MedicalSpecialtyDTO get(@PathVariable(name = "id") String id)
    {
        return medicalSpecialtyService.get(Long.parseLong(id));
    }
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MedicalSpecialtyDTO update(@PathVariable String id, @Valid @RequestBody MedicalSpecialtyDTO roomDTO)
    {
        return medicalSpecialtyService.update(roomDTO,Long.parseLong(id));
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
    @PostMapping("/{id}/add")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean add(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO)
    {
        return medicalSpecialtyService.addDoctor(doctorDTO,id);
    }
    @PostMapping("/{id}/remove")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean remove(@PathVariable Long id)
    {
        return medicalSpecialtyService.removeDoctor(id);
    }
}
