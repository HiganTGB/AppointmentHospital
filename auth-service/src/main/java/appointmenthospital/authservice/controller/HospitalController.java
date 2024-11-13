package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.dto.DoctorListDTO;
import appointmenthospital.authservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
// Public API
@RestController
@RequestMapping("/api/v1/hospitals")
@RequiredArgsConstructor
public class HospitalController {
        private final DoctorService doctorService;
        @GetMapping("/doctors")
        public List<DoctorListDTO> getDoctors()
        {
                return doctorService.getList();
        }
}
