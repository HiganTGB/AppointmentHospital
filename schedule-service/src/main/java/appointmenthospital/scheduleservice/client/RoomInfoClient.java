package appointmenthospital.scheduleservice.client;

import appointmenthospital.scheduleservice.model.domain.RoomDomain;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@FeignClient(name = "info-service",url = "http://localhost:10004", path = "api/v1/rooms")
public interface RoomInfoClient {
    @GetMapping("/domain/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RoomDomain getDomain(@PathVariable Long id);

    @GetMapping("/domain/{id}/rooms")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Get Rooms", description = "Get all room by specialties",tags = {"Public API"})
    public List<RoomDomain> getRooms(@PathVariable Long id);
}
