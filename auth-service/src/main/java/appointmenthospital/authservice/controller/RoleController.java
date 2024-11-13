package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.RoleDTO;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<Role> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
            @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return roleService.getPage(keyword,pageable);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoleDTO get(@PathVariable Long id)
    {
        return roleService.get(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoleDTO create(@RequestBody @Valid RoleDTO roleDTO)
    {
        return roleService.create(roleDTO);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoleDTO update(@PathVariable Long id,@RequestBody @Valid RoleDTO roleDTO)
    {
        return roleService.update(roleDTO,id);
    }

}
