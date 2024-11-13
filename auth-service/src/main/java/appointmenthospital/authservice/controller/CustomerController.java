package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.DoctorFilterRequest;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer API", description = "All about Customer")
@Order(4)
public class CustomerController {

    private final UserService userService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<UserDTO> getAll(
            @RequestParam(defaultValue = "",value = "search",required =false) String keyword,
            @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return userService.getPage(pageable,false,keyword);
    }
    @GetMapping("/{id}")
    public UserDTO get(@PathVariable(name = "id") Long id)
    {
        return userService.getDto(id);
    }
}
