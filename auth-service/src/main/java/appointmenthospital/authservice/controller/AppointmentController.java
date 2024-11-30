package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.AppointmentDTO;
import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class  AppointmentController {
    private AppointmentService appointmentService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    public Page<AppointmentDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
                                  @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                  @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                  @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy,
                                       @RequestParam(value = "doctor",required = false) long doctor_id,
                                       @RequestParam(value = "profile",required = false) long profile_id,
                                       @RequestParam(value = "patient",required = false) long patient_id

    ) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return appointmentService.getPaged(keyword,pageable,doctor_id,profile_id,patient_id);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AppointmentDTO get(@PathVariable Long id)
    {
        return appointmentService.get(id);
    }
//    @PostMapping
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public AppointmentDTO create(@RequestBody @Valid DoctorDTO dto)
//    {
//        return appointmentService.create(dto);
//    }
//    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public AppointmentDTO update(@PathVariable Long id,@RequestBody @Valid DoctorDTO dto)
//    {
//        return appointmentService.update(dto,id);
//    }
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Boolean delete(@PathVariable Long id)
//    {
//        return appointmentService.delete(id);
//    }
}
