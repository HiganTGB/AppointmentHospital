package appointmenthospital.thymeleafclient.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {
    @GetMapping("/admin")
    public String admin() {
        return "layouts/admin";
    }
    @GetMapping("/admin/foo")
    public String fooUI() {
        return "pages/admin/foo";
    }
}
