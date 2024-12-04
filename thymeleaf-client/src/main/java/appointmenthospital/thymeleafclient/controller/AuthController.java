package appointmenthospital.thymeleafclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @GetMapping("/sign-in")
    public String login()
    {
        return "pages/auth/sign-in";
    }
    @GetMapping("/register")
    public String register()
    {
        return "pages/auth/sign-up";
    }


}
