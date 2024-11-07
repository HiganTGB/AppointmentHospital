package appointmenthospital.thymeleafclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
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
