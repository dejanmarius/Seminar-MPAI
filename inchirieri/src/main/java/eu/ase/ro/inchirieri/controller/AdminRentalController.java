package eu.ase.ro.inchirieri.controller;

import eu.ase.ro.inchirieri.model.RentalStatus;
import eu.ase.ro.inchirieri.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interfata admin: vede toate cererile, filtreaza dupa stare/utilizator/echipament/data,
 * aproba/respinge, marcheaza ca preluata/returnata (cu efect pe disponibilitate).
 */
@Controller
@RequestMapping("/admin/rentals")
public class AdminRentalController {

    private final RentalService rentalService;

    public AdminRentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String status,
                       @RequestParam(required = false) String userName,
                       @RequestParam(required = false) String equipmentName,
                       @RequestParam(required = false) String date,
                       Model model) {
        model.addAttribute("rentals",
                rentalService.filter(status, userName, equipmentName, date));
        model.addAttribute("statuses", RentalStatus.values());
        return "admin/rentals/index";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("rental", rentalService.findById(id));
        model.addAttribute("statuses", RentalStatus.values());
        return "admin/rentals/detail";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String newStatus) {
        rentalService.updateStatus(id, newStatus);
        return "redirect:/admin/rentals/" + id;
    }
}
