package appointmenthospital.authservice.speedsms;


import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.xml.bind.DatatypeConverter;


public class SpeedSMSAPI {
    public static final String API_URL = "https://api.speedsms.vn/index.php";
    @Value("${speedSms.access-token}")
    protected String mAccessToken;
    @Value("${speedSms.sender}")
    protected String sender;
    private static final int EXPIRATION = 60 * 24;
    Map<String, String> otpMap = new HashMap<>();
    private Map<String, LocalDateTime> expirationMap = new HashMap<>();

    /**
     * Get user information
     * @param: none
     * @return: json string
     * */
    public String getUserInfo() throws IOException {
        URL url = new URL(API_URL + "/user/info");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        String userCredentials = mAccessToken + ":x";
        String basicAuth = "Basic " + DatatypeConverter.printBase64Binary(userCredentials.getBytes());
        conn.setRequestProperty ("Authorization", basicAuth);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine = "";
        StringBuffer buffer = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            buffer.append(inputLine);
        }
        in.close();
        return buffer.toString();
    }

    /**
     * Send SMS
     * @param
     * @return: json string
     * */
    public String sendSMS(String to, String content, int type, String sender) throws IOException {
        String json = "{\"to\": [\"" + to + "\"], \"content\": \"" + EncodeNonAsciiCharacters(content) + "\", \"type\":" + type + ", \"brandname\":\"" + sender + "\"}";
        URL url = new URL(API_URL + "/sms/send");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        String userCredentials = mAccessToken + ":x";
        String basicAuth = "Basic " + DatatypeConverter.printBase64Binary(userCredentials.getBytes());
        conn.setRequestProperty ("Authorization", basicAuth);
        conn.setRequestProperty("Content-Type", "application/json");

        conn.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(json);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine = "";
        StringBuffer buffer = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            buffer.append(inputLine);
        }
        in.close();
        return buffer.toString();
    }


    private String EncodeNonAsciiCharacters(String value) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            int unit = (int)c;
            if (unit > 127) {
                String hex = String.format("%04x", (int)unit);
                String encodedValue = "\\u" + hex;
                sb.append(encodedValue);
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
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

    public String sentOtp(String to) throws IOException {
        String otp=generateOTP();
        otpMap.put(to,otp);
        expirationMap.put(to,LocalDateTime.now().plusMinutes(EXPIRATION));
        return sendSMS(to,otp,4,sender);
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
}
