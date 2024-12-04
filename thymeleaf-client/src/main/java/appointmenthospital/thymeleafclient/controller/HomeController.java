package appointmenthospital.thymeleafclient.controller;

import appointmenthospital.thymeleafclient.model.UserDTO;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(12345L);
        userDTO.setEnabled(true);
        userDTO.setEmail("hxb3011@outlook.com");
        userDTO.setFullName("Huynh Xuan Bach");
        userDTO.setPhone("0123456789");
        model.addAttribute("user", userDTO);
        return "clients/pages/home";
    }
}
