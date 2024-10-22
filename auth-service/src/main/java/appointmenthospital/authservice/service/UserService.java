package appointmenthospital.authservice.service;

import appointmenthospital.authservice.model.dto.ChangePasswordRequest;
import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.model.entity.User;
import appointmenthospital.authservice.repository.RoleRepository;
import appointmenthospital.authservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    public void initUser()
    {
        Role role=new Role();
        role.setName("ADMIN");
        role.setPermissionGranted(1,true);
        role.setPermissionGranted(2,false);
        role.setPermissionGranted(3,true);
        roleRepository.save(role);

        var user = User.builder()
                .firstName("admin")
                .lastName("admin")
                .phone("0386152395")
                .role(roleRepository.getReferenceById(1L))
                .isEnabled(true)
                .isStaff(true)
                .emailVerified(false)
                .phoneVerified(false)
                .password(passwordEncoder.encode("admin"))
                .build();
        repository.save(user);
    }
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }
}