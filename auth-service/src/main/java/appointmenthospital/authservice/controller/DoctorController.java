package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.dto.DoctorDomain;
import appointmenthospital.authservice.model.dto.DoctorFilterRequest;
import appointmenthospital.authservice.model.dto.DoctorRequest;
import appointmenthospital.authservice.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DoctorDTO get(@PathVariable Long id)
    {
        return doctorService.get(id);
    }

    @GetMapping("/{id}/domain")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DoctorDomain getDomain(@PathVariable Long id)
    {
        return new DoctorDomain(doctorService.getEntity(id)) ;
    }
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DoctorDTO create(@Valid @RequestPart(name = "doctor") DoctorRequest doctorRequest,@RequestPart("image") MultipartFile file)
    {
        return doctorService.create(doctorRequest,file);
    }
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DoctorDTO update(@Valid @RequestPart(name = "doctor") DoctorRequest doctorRequest,@RequestPart("image") MultipartFile file,@PathVariable Long id)
    {
        return doctorService.update(doctorRequest,file,id);
    }
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
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
