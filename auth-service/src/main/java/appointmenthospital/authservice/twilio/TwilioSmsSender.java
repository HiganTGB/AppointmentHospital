package appointmenthospital.authservice.twilio;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import appointmenthospital.authservice.exc.AppException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("twilio")
public class TwilioSmsSender{
    Map<String, String> otpMap = new HashMap<>();
    private Map<String, LocalDateTime> expirationMap = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSmsSender.class);
    private final TwilioConfiguration twilioConfiguration;
    private static final int EXPIRATION = 60 * 24;
    @Autowired
    public TwilioSmsSender(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }
    public String sendOTP(String phone)
    {
        try {
            PhoneNumber to = new PhoneNumber(phone);
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
            String messagebody = generateOTP();
            Message message = Message.creator(to, from, messagebody).create();
            otpMap.put(phone,messagebody);
            expirationMap.put(phone,LocalDateTime.now().plusMinutes(EXPIRATION));
            return messagebody;
        } catch (Exception ex) {
           throw new AppException("Cannot sent sms");
        }
    }
    public Boolean validateOTP(String userInputOtp,String phone) {
        LocalDateTime expiration = expirationMap.get(phone);
        if (expiration != null && LocalDateTime.now().isAfter(expiration)) {
            otpMap.remove(phone);
            expirationMap.remove(phone);
            return false;
        }
        if (userInputOtp.equals(otpMap.get(phone))) {
            otpMap.remove(phone,userInputOtp);
            return true;
        } else {
            return false;
        }
    }
    private  String generateOTP() {
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(numbers.length());
            sb.append(numbers.charAt(index));
        }

        return sb.toString();
    }
}
