package appointmenthospital.thymeleafclient.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("")
    public String admin() {
        return "layouts/admin";
    }
    @GetMapping("/foo")
    public String fooUI() {
        return "pages/admin/foo/foo";
    }
}
