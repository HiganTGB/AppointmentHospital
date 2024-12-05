package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.ProfileDTO;
import appointmenthospital.authservice.model.dto.RoleDTO;
import appointmenthospital.authservice.model.entity.Permission;
import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.service.ProfileService;
import appointmenthospital.authservice.service.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    @PreAuthorize("hasAuthority('ReadProfile')")
    public List<ProfileDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
                                   @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                   @RequestParam(value="sortBy" ,required = false,defaultValue = "fullName") String sortBy,
                                   @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return profileService.getPaged(keyword,pageable).getContent();
    }
    @GetMapping("/user/current")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    public List<ProfileDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
                                   @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                   @RequestParam(value="sortBy" ,required = false,defaultValue = "fullName") String sortBy,
                                   @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy, Principal connectedUser) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return profileService.getPaged(keyword,pageable, userService.get(connectedUser).getId()).getContent();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ReadProfile')")
    public ProfileDTO get(@PathVariable Long id)
    {
        return profileService.get(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('CreateProfile')")
    public ProfileDTO create(@RequestBody @Valid ProfileDTO profileDTO)
    {
        return profileService.create(profileDTO);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('UpdateProfile')")
    public ProfileDTO update(@PathVariable Long id,@RequestBody @Valid ProfileDTO profileDTO)
    {
        return profileService.update(profileDTO,id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('DeleteProfile')")
    public Boolean delete(@PathVariable Long id)
    {
        return profileService.delete(id);
    }
}
