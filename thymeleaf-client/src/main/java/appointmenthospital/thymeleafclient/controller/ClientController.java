package appointmenthospital.thymeleafclient.controller;

import appointmenthospital.thymeleafclient.model.authservice.ProfileDTO;
import appointmenthospital.thymeleafclient.model.authservice.UserDTO;
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

    @GetMapping(value = {"/", "/home"})
    public String home(Model model) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(12345L);
//        userDTO.setEnabled(true);
//        userDTO.setEmail("hxb3011@outlook.com");
//        userDTO.setFullName("Huynh Xuan Bach");
//        userDTO.setPhone("0123456789");
//        model.addAttribute("user", userDTO);
        return "pages/clients/home";
    }

    @GetMapping("/appointment")
    public String appointment() {
        return "pages/clients/appointment";
    }

    @GetMapping("/profile")
    public String profile() {
        return "pages/clients/profile";
    }

    @GetMapping("/scheduler")
    public String scheduler() {
        return "pages/clients/scheduler";
    }
}
