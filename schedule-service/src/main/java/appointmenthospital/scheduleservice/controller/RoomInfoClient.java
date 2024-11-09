package appointmenthospital.scheduleservice.controller;

import appointmenthospital.scheduleservice.model.dto.RoomDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "info-service",url = "http://localhost:10004", path = "api/v1/room")
public interface RoomInfoClient {
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RoomDTO get(@PathVariable Long id);
}
