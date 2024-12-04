package appointmenthospital.thymeleafclient.service;

import appointmenthospital.thymeleafclient.model.authservice.ProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private RestTemplate restTemplate;

    public ProfileDTO getProfileByID(int id) {
        String url = "http://localhost:10001/api/v1/profile/" + id;
        return restTemplate.getForObject(url, ProfileDTO.class);
    }
}
