package appointmenthospital.thymeleafclient.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/specialty")
public class MedicalSpecialtyController {
    @GetMapping()
    public String index()
    {
        return "layouts/admin";
    }
    @GetMapping("/admin/foo")
    public String fooUI() {
        return "pages/admin/foo";
    }
}
