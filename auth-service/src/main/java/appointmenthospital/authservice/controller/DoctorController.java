package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.dto.DoctorFilterRequest;
import appointmenthospital.authservice.model.dto.DoctorRequest;
import appointmenthospital.authservice.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    @GetMapping("/{id}")
    public DoctorDTO get(@PathVariable Long id)
    {
        return doctorService.get(id);
    }
    @PostMapping
    public DoctorDTO create(@Valid @RequestBody DoctorRequest doctorRequest)
    {
        return doctorService.create(doctorRequest);
    }
    @PutMapping("/{id}")
    public DoctorDTO update(@Valid @RequestBody DoctorRequest doctorRequest,@PathVariable Long id)
    {
        return doctorService.update(doctorRequest,id);
    }
    @GetMapping
    public Page<DoctorDTO> getPage(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
                                   @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                   @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                   @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy,
                                   DoctorFilterRequest request)
    {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10
        return doctorService.getPage(keyword,pageable,request);
    }
}
