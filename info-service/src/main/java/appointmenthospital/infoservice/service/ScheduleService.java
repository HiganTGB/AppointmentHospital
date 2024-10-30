package appointmenthospital.infoservice.service;

import appointmenthospital.infoservice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private ScheduleRepository scheduleRepository;
}
