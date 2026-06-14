package eu.ase.ro.inchirieri.controller;

import eu.ase.ro.inchirieri.dto.request.RentalRequestDto;
import eu.ase.ro.inchirieri.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Interfata utilizator: vede cererile proprii, face o cerere noua (alege
 * echipament + perioada + scop) si anuleaza inainte de aprobare. NU schimba stari.
 */
@Controller
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String userName, Model model) {
        model.addAttribute("rentals", rentalService.filter(null, userName, null, null));
        return "rentals/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("rental", new RentalRequestDto());
        model.addAttribute("equipments", rentalService.getAvailableEquipments());
        return "rentals/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute RentalRequestDto request) {
        rentalService.create(request);
        return "redirect:/rentals";
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        rentalService.cancelByUser(id);
        return "redirect:/rentals";
    }
}
