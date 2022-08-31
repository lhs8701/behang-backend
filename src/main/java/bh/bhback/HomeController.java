package bh.bhback;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping ("/api/usage")
    public String api(){
        return "redirect:/swagger-ui/index.html";
    }
}
