package eu.ase.ro.inchirieri.controller;

import eu.ase.ro.inchirieri.dto.request.RentalRequestDto;
import eu.ase.ro.inchirieri.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Interfata utilizator: vede cererile, face o cerere noua si anuleaza inainte de aprobare.
// NU schimba stari — asta e exclusiv pentru admin.
@Controller
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // GET /rentals — lista cererilor, cu filtru optional dupa numele utilizatorului
    // status=null, equipmentName=null, date=null -> utilizatorul nu filtreaza dupa acestea
    @GetMapping
    public String list(@RequestParam(required = false) String userName, Model model) {
        model.addAttribute("rentals", rentalService.filter(null, userName, null, null));
        return "rentals/index";
    }

    // GET /rentals/add — formular cerere noua
    // trimitem doar echipamentele DISPONIBILE in dropdown (nu toate)
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("rental", new RentalRequestDto());
        model.addAttribute("equipments", rentalService.getAvailableEquipments());
        return "rentals/edit";
    }

    // POST /rentals/save — salveaza cererea si redirecteaza (PRG pattern)
    // @ModelAttribute leaga automat campurile din form pe RentalRequestDto
    @PostMapping("/save")
    public String save(@ModelAttribute RentalRequestDto request) {
        rentalService.create(request);
        return "redirect:/rentals"; // PRG: evita retrimiterea formularului la refresh
    }

    // POST /rentals/{id}/cancel — anulare de catre utilizator
    // service-ul valideaza ca starea e CERUTA (singura stare anulabila de utilizator)
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        rentalService.cancelByUser(id);
        return "redirect:/rentals";
    }
}
