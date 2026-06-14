package eu.ase.ro.schelet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Raspunde la http://localhost:8080/home/ si http://localhost:8080/home/home
    @GetMapping({"/", "/home"})
    public String index() {
        return "index"; // -> templates/index.html
    }
}
