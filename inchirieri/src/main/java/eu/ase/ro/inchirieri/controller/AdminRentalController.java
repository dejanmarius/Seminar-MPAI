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

// Interfata admin: vede toate cererile, filtreaza si schimba stari.
// La APROBATA echipamentul devine indisponibil; la RETURNATA/RESPINSA revine disponibil.
@Controller
@RequestMapping("/admin/rentals")
public class AdminRentalController {

    private final RentalService rentalService;

    public AdminRentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // GET /admin/rentals — lista cu toate cererile, toate filtrele optionale
    // parametrii cu required=false sunt null daca nu sunt trimisi din form
    @GetMapping
    public String list(@RequestParam(required = false) String status,
                       @RequestParam(required = false) String userName,
                       @RequestParam(required = false) String equipmentName,
                       @RequestParam(required = false) String date,
                       Model model) {
        model.addAttribute("rentals",
                rentalService.filter(status, userName, equipmentName, date));
        // trimitem toate valorile enum-ului pentru dropdown-ul de filtrare dupa stare
        model.addAttribute("statuses", RentalStatus.values());
        return "admin/rentals/index";
    }

    // GET /admin/rentals/{id} — detalii cerere + form schimbare stare
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("rental", rentalService.findById(id));
        model.addAttribute("statuses", RentalStatus.values());
        return "admin/rentals/detail";
    }

    // POST /admin/rentals/{id}/status — schimba starea
    // service-ul valideaza tranzitia si actualizeaza disponibilitatea echipamentului
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String newStatus) {
        rentalService.updateStatus(id, newStatus);
        return "redirect:/admin/rentals/" + id;
    }
}
