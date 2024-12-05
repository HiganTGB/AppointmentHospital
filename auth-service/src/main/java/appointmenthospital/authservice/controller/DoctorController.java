package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.client.FileStorageClient;
import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.dto.PatientDTO;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.service.DoctorService;
import appointmenthospital.authservice.service.UserService;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {
    private final UserService userService;


    private final DoctorService doctorService;
    private final FileStorageClient storageClient;
    @Autowired
    public DoctorController(UserService userService, DoctorService doctorService, FileStorageClient storageClient) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.storageClient = storageClient;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    public List<DoctorDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
                                  @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                  @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                  @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return doctorService.getPaged(keyword,pageable).getContent();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ReadUser')")
    public DoctorDTO get(@PathVariable Long id)
    {
        return doctorService.get(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('CreateUser')")
    public DoctorDTO create(@RequestBody @Valid DoctorDTO dto)
    {
        return doctorService.create(dto);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('UpdateUser')")
    public DoctorDTO update(@PathVariable Long id,@RequestBody @Valid DoctorDTO dto)
    {
        return doctorService.update(dto,id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('DeleteUser')")
    public Boolean delete(@PathVariable Long id)
    {
        return doctorService.delete(id);
    }
    @GetMapping("/{doctor_id}/image")
    public String setImage(@PathVariable Long doctor_id,@RequestPart("image") MultipartFile file)
    {
        DoctorDTO dto=doctorService.get(doctor_id);
        return userService.setImage(file,dto.getId());
    }
    @PostMapping("/{doctor_id}/image")

    public ResponseEntity<?> getImage(@PathVariable Long doctor_id)
    {
        DoctorDTO dto=doctorService.get(doctor_id);
        String link= userService.getImage( dto.getUserId());
        try
        {
            return storageClient.downloadImageFromFileSystem(link);
        }catch (FeignException e)
        {
            return null;
        }
    }
    @GetMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DoctorDTO get(Principal connectedUser)
    {
        return doctorService.getByUser(connectedUser);
    }

}
