package eu.ase.ro.skeleton.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home() {
        // Redirectionam catre lista de itemi
        return "redirect:/items";
    }
}
