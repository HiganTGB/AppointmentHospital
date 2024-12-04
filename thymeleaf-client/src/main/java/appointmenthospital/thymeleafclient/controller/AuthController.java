package appointmenthospital.thymeleafclient.controller;

import appointmenthospital.thymeleafclient.model.authservice.RegisterRequestDTO;
import appointmenthospital.thymeleafclient.model.authservice.UserDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindingResult;


import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {
    public static boolean isLoggedIn = false;

    @GetMapping("/login")
    public String login()
    {
        return "pages/auth/sign-in";
    }

    @GetMapping("/register")
    public String register(Model model)
    {
        model.addAttribute("userregister", new RegisterRequestDTO());
        return "pages/auth/sign-up";
    }

    @PostMapping("/register")
    public String register(Model model,
                           @ModelAttribute("userregister") RegisterRequestDTO registerRequestDTO,
                           BindingResult result)
    {

        return "redirect:/home";
    }

    @GetMapping("/getLoggedStatus")
    @ResponseBody
    public Map<String, Boolean> getLoggedStatus() {
        boolean isLogged = checkIfLoggedIn();
        Map<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("isLogged", isLogged);
        return response; // Trả về JSON với trạng thái đăng nhập
    }

    private boolean checkIfLoggedIn() {
        return isLoggedIn;
    }
}
