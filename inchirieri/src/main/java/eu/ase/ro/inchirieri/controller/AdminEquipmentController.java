package eu.ase.ro.inchirieri.controller;

import eu.ase.ro.inchirieri.dto.request.EquipmentRequest;
import eu.ase.ro.inchirieri.service.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// CRUD complet pentru catalogul de echipamente — doar admin.
// Acelasi template edit.html e folosit atat pentru adaugare cat si pentru editare
// (diferentiat prin flag-ul "editing" trimis in model).
@Controller
@RequestMapping("/admin/equipments")
public class AdminEquipmentController {

    private final EquipmentService equipmentService;

    public AdminEquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // GET /admin/equipments — lista tuturor echipamentelor (disponibile si indisponibile)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("equipments", equipmentService.getAll());
        return "admin/equipments/index";
    }

    // GET /admin/equipments/add — formular adaugare echipament nou
    // editing=false -> template afiseaza "Adauga" si action spre /save
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("equipment", new EquipmentRequest());
        model.addAttribute("editing", false);
        return "admin/equipments/edit";
    }

    // POST /admin/equipments/save — salveaza echipamentul nou
    @PostMapping("/save")
    public String save(@ModelAttribute EquipmentRequest request) {
        equipmentService.create(request);
        return "redirect:/admin/equipments";
    }

    // GET /admin/equipments/{id}/edit — formular editare echipament existent
    // editing=true -> template afiseaza "Editeaza" si action spre /{id}/update
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("equipment", equipmentService.findById(id));
        model.addAttribute("editing", true);
        return "admin/equipments/edit"; // acelasi template ca la adaugare
    }

    // POST /admin/equipments/{id}/update — salveaza modificarile
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute EquipmentRequest request) {
        equipmentService.update(id, request);
        return "redirect:/admin/equipments";
    }

    // POST /admin/equipments/{id}/delete — sterge echipamentul
    // folosim POST (nu DELETE) pentru ca formularele HTML suporta doar GET si POST
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return "redirect:/admin/equipments";
    }
}
