package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.client.FileStorageClient;
import appointmenthospital.authservice.model.dto.PatientDTO;
import appointmenthospital.authservice.model.dto.ProfileDTO;
import appointmenthospital.authservice.model.dto.RoleDTO;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.model.dtoOld.ChangePasswordRequest;
import appointmenthospital.authservice.service.PatientService;
import appointmenthospital.authservice.service.UserService;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "All about User")
@Order(2)
public class UserController {

    private final UserService userService;
    private final PatientService patientService;
    private final FileStorageClient storageClient;
    @Autowired
    public UserController(UserService userService, PatientService patientService, FileStorageClient storageClient) {
        this.userService = userService;
        this.patientService = patientService;
        this.storageClient = storageClient;
    }

    //    @Operation(summary = "Lock", description = "Lock account")
//    @PostMapping("/{id}/lock")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Boolean lock(@PathVariable(name = "id") Long id)
//    {
//        return service.lock(id);
//    }
//    @Operation(summary = "Unlock", description = "Unlock account")
//    @DeleteMapping("/{id}/lock")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Boolean unlock(@PathVariable(name = "id") Long id)
//    {
//        return service.unlock(id);
//    }
    @Operation(summary = "Password", description = "Change password", tags = { "Need Principal" })
    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
//    @Operation(summary = "update Profile", description = "update User", tags = { "Need Principal" })
//    @PutMapping
//    public UserDTO update(
//            @Valid @RequestBody UserRequest userRequest,
//            Principal connectedUser)
//    {
//            return service.update(userRequest,connectedUser);
//    }
    @PostMapping("/resetPassword")
    public void resetPassword(HttpServletRequest request,
                              @RequestParam("phone") String phone) {

    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    public Page<PatientDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
                                   @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                   @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                   @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return patientService.getPaged(keyword,pageable);
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
    public PatientDTO create(@RequestBody @Valid PatientDTO patientDTO)
    {
        return patientService.create(patientDTO);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PatientDTO update(@PathVariable Long id,@RequestBody @Valid PatientDTO patientDTO)
    {
        return patientService.update(patientDTO,id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean delete(@PathVariable Long id)
    {
        return patientService.delete(id);
    }

    @PutMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO update(Principal connectedUser,@RequestBody @Valid UserDTO userDTO)
    {
        return userService.update(userDTO,connectedUser);
    }
// End of user
    @GetMapping("/{patent_id}/image")
    public String setImage(@PathVariable Long patent_id,@RequestPart("image") MultipartFile file)
    {
        PatientDTO patientDTO=patientService.get(patent_id);
        return userService.setImage(file,patientDTO.getUserId());
    }
    @PostMapping("/{patent_id}/image")
    public ResponseEntity<?> getImage(@PathVariable Long patent_id)
    {
        PatientDTO patientDTO=patientService.get(patent_id);
        String link= userService.getImage(patientDTO.getUserId());
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
    public UserDTO get(Principal connectedUser)
    {
        return userService.get(connectedUser);
    }

    @GetMapping("/current/image")
    public String setImage(@RequestPart("image") MultipartFile file,Principal connectedUser)
    {
        return userService.setImage(file,connectedUser);
    }
    @PostMapping("/current/image")
    public ResponseEntity<?> getImage(Principal connectedUser)
    {
        String link= userService.getImage(connectedUser);
        try
        {
            return storageClient.downloadImageFromFileSystem(link);
        }catch (FeignException e)
        {
            return null;
        }
    }

}