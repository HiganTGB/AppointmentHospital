package appointmenthospital.infoservice.controller;

import appointmenthospital.infoservice.model.dto.RoomDTO;
import appointmenthospital.infoservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RoomDTO get(@PathVariable String id)
    {
        return roomService.get(Long.parseLong(id));
    }
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RoomDTO update(@PathVariable String id, @Valid @RequestBody RoomDTO roomDTO)
    {
        return roomService.update(roomDTO,Long.parseLong(id));
    }
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RoomDTO create(@Valid @RequestBody RoomDTO roomDTO)
    {
        return roomService.create(roomDTO);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<RoomDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
            @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return roomService.getPage(keyword,pageable);
    }

}
