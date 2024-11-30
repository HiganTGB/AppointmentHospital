package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.RoleDTO;
import appointmenthospital.authservice.model.entity.Permission;
import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.service.RoleService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role API", description = "All about Role")
@Order(5)
public class RoleController {
    private final RoleService roleService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    public Page<RoleDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean delete(@PathVariable Long id)
    {
        return roleService.delete(id);
    }

    @GetMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Permission> getPermission(@PathVariable Long id)
    {
        return roleService.get(id).getPermissions();
    }
    @PostMapping("/{id}/permissions/{permit}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoleDTO changePermission(@PathVariable Long id,@PathVariable int permit,
                                    @RequestParam(value="granted" ,required = true) Boolean granted)
    {
        return roleService.changePermission(id,permit,granted);
    }


    @GetMapping("/permissions")
    @Operation(summary = "Permission", description = "Get all permission list")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Permission> getPermission()
    {
        return List.of(Permission.values());
    }
}
