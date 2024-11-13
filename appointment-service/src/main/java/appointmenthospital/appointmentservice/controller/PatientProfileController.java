package appointmenthospital.appointmentservice.controller;

import appointmenthospital.appointmentservice.model.dto.PatientDTO;
import appointmenthospital.appointmentservice.model.entity.PatientProfile;
import appointmenthospital.appointmentservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientProfileController {
    private final PatientService patientService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<PatientDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
                                   @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                   @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                   @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return patientService.getPage(keyword,pageable);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PatientDTO get(@PathVariable Long id)
    {
        return patientService.get(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PatientDTO create(@RequestBody @Valid PatientDTO patientDTO, Principal principal)
    {
        return patientService.create(patientDTO,principal);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PatientDTO update(@PathVariable Long id,@RequestBody @Valid PatientDTO roleDTO)
    {
        return patientService.update(roleDTO,id);
    }
    @GetMapping("/{id}/patients")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PatientDTO> getAllByCustomer(@PathVariable Long id)
    {
        return patientService.getAllByAccountID(id);
    }
}
