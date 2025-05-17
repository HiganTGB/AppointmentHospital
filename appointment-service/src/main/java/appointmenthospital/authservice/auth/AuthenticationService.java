package appointmenthospital.authservice.auth;

import appointmenthospital.authservice.config.JwtService;
import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dtoOld.PasswordResetDTO;
import appointmenthospital.authservice.model.entity.PasswordResetToken;
import appointmenthospital.authservice.model.entity.User;
import appointmenthospital.authservice.repository.PasswordResetTokenRepository;
import appointmenthospital.authservice.repository.UserRepository;
import appointmenthospital.authservice.service.UserService;
import appointmenthospital.authservice.speedsms.SpeedSMSAPI;
import appointmenthospital.authservice.token.Token;
import appointmenthospital.authservice.token.TokenRepository;
import appointmenthospital.authservice.token.TokenType;
import appointmenthospital.authservice.twilio.TwilioSmsSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomLogger logger;
    private final UserService userService;
    private final SpeedSMSAPI smsSender=new SpeedSMSAPI();
    public boolean sentOTP(String phone)
    {
        try {
            String otp= smsSender.sentOtp(phone);
            return true;
        }catch (AppException e)
        {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public AuthenticationResponse register(RegisterRequest request) {
        if(phoneExist(request.getPhone()))
        {
            throw new IllegalStateException("Phone already exists");
        }
        if(emailExist(request.getEmail()))
        {
            throw new IllegalStateException("Email already exists");
        }
//        if(smsSender.validateOTP(request.getOtp(),request.getPhone()))
//        {
//            throw new IllegalStateException("Invalid OTP");
//        }
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .isEnabled(true)
                .isStaff(false)
                .emailVerified(false)
                .phoneVerified(false)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhone(),
                        request.getPassword()
                )
        );
        var user = repository.findByPhone(request.getPhone())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        logger.log(this.getClass().toString(),"User with id "+ user.getId() + " login success");
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userPhone;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userPhone = jwtService.extractUsername(refreshToken);
        if (userPhone != null) {
            User user = this.repository.findByPhone(userPhone)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


    public boolean createResetPassword(String phone) {

        var user = repository.findByPhone(phone)
                .orElseThrow();

        try {
            String otp= smsSender.sentOtp(phone);
            return true;
        }catch (AppException e)
        {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
    public String authResetOTPPassword(String otp,String phone)
    {
        smsSender.validateOTP(otp,phone);
        String token= UUID.randomUUID().toString();
        var user = repository.findByPhone(phone)
                .orElseThrow();
        PasswordResetToken resetToken=new PasswordResetToken(token,user);
        passwordResetTokenRepository.save(resetToken);
        return token;
    }
    public AuthenticationResponse resetPassword(PasswordResetDTO dto)
    {
        PasswordResetToken token=passwordResetTokenRepository.getByToken(dto.getToken());
        var user = repository.findByPasswordResetToken(token)
                .orElseThrow();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private boolean emailExist(String email) {
        return repository.existsByEmail(email);
    }
    private boolean phoneExist(String phone) {
        return repository.existsByPhone(phone);
    }
    private boolean emailExist(String email,String oldEmail) {
        return repository.existsByEmailAndEmailNotLike(email,oldEmail);
    }
    private boolean phoneExist(String phone,String oldPhone) {
        return repository.existsByPhoneAndPhoneNotLike(phone,oldPhone);
    }
}