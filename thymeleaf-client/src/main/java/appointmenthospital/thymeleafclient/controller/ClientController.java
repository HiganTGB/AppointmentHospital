package appointmenthospital.thymeleafclient.controller;

import appointmenthospital.thymeleafclient.dto.ProfileDTO;
import appointmenthospital.thymeleafclient.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ClientController {
    private ProfileService profileService;

    public ClientController(){
        profileService = new ProfileService();
    }

    @GetMapping("/test")
    public String client(Model model) {
        ProfileDTO profileDTO = profileService.getProfileByID(1);
        model.addAttribute("thanhVien", profileDTO);
        return "pages/main/profile";
    }

    @GetMapping("/")
    public String index() {
        return "pages/client/index";
    }
}
