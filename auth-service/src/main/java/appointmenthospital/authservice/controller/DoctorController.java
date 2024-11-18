package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.*;
import appointmenthospital.authservice.model.entity.Degree;
import appointmenthospital.authservice.model.entity.Gender;
import appointmenthospital.authservice.service.DoctorService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@Tag(name = "Doctor API", description = "All about doctor")
@Order(3)
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

    @GetMapping("/domain/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Hidden
    public DoctorDomain getDomain(@PathVariable Long id)
    {
        return new DoctorDomain(doctorService.getEntity(id)) ;
    }
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "create Doctor", description = "create Doctor with image, set multipart/form-data")
    public DoctorDTO create(@Valid @RequestPart(name = "doctor") DoctorRequest doctorRequest,@RequestPart("image") MultipartFile file)
    {
        return doctorService.create(doctorRequest,file);
    }
    @PutMapping(value = "/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "update Doctor", description = "update Doctor with image,set multipart/form-data")
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
    @Operation(summary = "get Doctors", description = "get DoctorList", tags = { "Public API" })
    @GetMapping("/public/lists")
    public List<DoctorListDTO> getDoctors(
            @RequestParam(defaultValue = "",value = "search",required =false) String keyword,
            DoctorFilterRequest request
    )
    {
        return doctorService.getList(keyword,request);
    }
}
