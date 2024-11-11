package appointmenthospital.authservice.service;

import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.model.dto.ChangePasswordRequest;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.model.dto.UserRequest;
import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.model.entity.User;
import appointmenthospital.authservice.repository.RoleRepository;
import appointmenthospital.authservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final Type pageType = new TypeToken<Page<UserDTO>>(){}.getType();
    @Value("${application.security.password.default-prefix}")
    private long passwordPrefix;
   // @PostConstruct
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
                .phone("0123456789")
                .email("admin@gmail.com")
                .role(roleRepository.getReferenceById(1L))
                .isEnabled(true)
                .isStaff(true)
                .emailVerified(false)
                .phoneVerified(false)
                .password(passwordEncoder.encode("admin"))
                .build();
        userRepository.save(user);
    }
    public UserDTO create(UserRequest userDto) // create staff only
    {
        if(emailExist(userDto.getEmail()))
        {
            throw new IllegalStateException("Email already exists");
        }
        if(phoneExist(userDto.getPhone()))
        {
            throw new IllegalStateException("Phone already exists");
        }
        var user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .role(null)
                .isEnabled(true)
                .isStaff(true)
                .emailVerified(true)
                .phoneVerified(true)
                .password(passwordEncoder.encode(passwordPrefix+userDto.getPhone()))
                .build();
        return modelMapper.map(userRepository.save(user),UserDTO.class) ;
    }
    public UserDTO update(UserRequest userDto, Long id) {
        User old=get(id);
        old.setFirstName(userDto.getFirstName());
        old.setLastName(userDto.getLastName());
        if(phoneExist(userDto.getPhone(),old.getPhone()))
        {
            throw new IllegalStateException("Phone already exists");
        }
        if(emailExist(userDto.getEmail(),old.getEmail()))
        {
            throw new IllegalStateException("Email already exists");
        }
        old.setPhone(userDto.getPhone());
        old.setEmail(userDto.getEmail());
        return modelMapper.map(userRepository.save(old),UserDTO.class) ;
    }
    public boolean lock(Long id)
    {
        User old=get(id);
        old.setEnabled(false);
        userRepository.save(old);
        return true;
    }
    public boolean unlock(Long id)
    {
        User old=get(id);
        old.setEnabled(true);
        userRepository.save(old);
        return true;
    }
    private User get(Long id){
        try {
            return userRepository.getReferenceById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AppException("User with " + id +" not found");
        }
    }
    public UserDTO getDto(Long id)
    {
        try {
            User user=get(id);
            return modelMapper.map(user,UserDTO.class);
        }catch (MappingException e)
        {
            throw new AppException("User with " + id +" not found");
        }

    }
    public Page<UserDTO> getPage(Pageable pageable)
    {
      return   modelMapper.map(userRepository.findAll(pageable),pageType);
    }
    public Page<UserDTO> getPage(Pageable pageable, boolean isStaff)
    {

        return (isStaff)? modelMapper.map(userRepository.findAllByIsStaffTrue(pageable),pageType)
                :
                modelMapper.map(userRepository.findAllByIsStaffFalse(pageable),pageType);
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
        userRepository.save(user);
    }
    private boolean emailExist(String email) {
        return userRepository.existsByEmail(email);
    }
    private boolean phoneExist(String phone) {
        return userRepository.existsByPhone(phone);
    }
    private boolean emailExist(String email,String oldEmail) {
        return userRepository.existsByEmailAndEmailNotLike(email,oldEmail);
    }
    private boolean phoneExist(String phone,String oldPhone) {
        return userRepository.existsByPhoneAndPhoneNotLike(phone,oldPhone);
    }

}