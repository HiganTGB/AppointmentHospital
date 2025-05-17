package appointmenthospital.authservice.service;

import appointmenthospital.authservice.client.FileStorageClient;
import appointmenthospital.authservice.model.dto.PatientDTO;
import appointmenthospital.authservice.model.dtoOld.ChangePasswordRequest;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.model.entity.Patient;
import appointmenthospital.authservice.model.entity.User;
import appointmenthospital.authservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageClient fileStorageClient;
    private ModelMapper modelMapper;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,FileStorageClient fileStorageClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageClient=fileStorageClient;
    }
    public UserDTO create(UserDTO userDto,boolean isStaff)
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
                .fullName(userDto.getFullName())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .role(null)
                .isEnabled(true)
                .isStaff(isStaff)
                .emailVerified(true)
                .phoneVerified(true)
                .password(passwordEncoder.encode(userDto.getPhone()))
                .build();
        return new UserDTO(userRepository.save(user)) ;
    }
    public UserDTO update(UserDTO userDto,boolean isStaff,long id)
    {
        User user=getEntity(id);
        if(emailExist(userDto.getEmail(),user.getEmail()))
        {
            throw new IllegalStateException("Email already exists");
        }
        if(phoneExist(userDto.getPhone(),user.getPhone()))
        {
            throw new IllegalStateException("Phone already exists");
        }
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setIsStaff(isStaff);
        return new UserDTO(userRepository.save(user)) ;
    }
    public User getEntity(long id)
    {
        return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User not found"));
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
    private User getEntity(Long id){
        return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User with " + id +" not found"));
    }
    public boolean lock(Long id)
    {
        User old= getEntity(id);
        old.setEnabled(false);
        userRepository.save(old);
        return true;
    }
    public boolean unlock(Long id)
    {
        User old= getEntity(id);
        old.setEnabled(true);
        userRepository.save(old);
        return true;
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
    public UserDTO update(UserDTO dto,Principal connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        return new UserDTO(userRepository.save(user));
    }
    @Transactional
    public String setImage(MultipartFile file,Principal connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return setImage(file,user.getId());
    }
    public String getImage(Principal connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return getImage(user.getId());
    }
    @Transactional
    public String setImage(MultipartFile file,long user_id)
    {
        User user=getEntity(user_id);
        var response = fileStorageClient.uploadImageToFileSystem(file);
        if(user.getImage()!=null)
        {
            fileStorageClient.deleteImageFromFileSystem(user.getImage());
        }
        user.setImage(response.getBody());
        userRepository.save(user);
        return user.getImage();
    }
    public String getImage(long user_id)
    {
        User user=getEntity(user_id);
        return user.getImage();
    }

    public UserDTO get(Principal connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return new UserDTO(getEntity(user.getId()));
    }

}
