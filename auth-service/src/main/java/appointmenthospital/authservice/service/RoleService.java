package appointmenthospital.authservice.service;

import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
}
