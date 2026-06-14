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

/** CRUD complet pentru catalogul de echipamente (doar admin). */
@Controller
@RequestMapping("/admin/equipments")
public class AdminEquipmentController {

    private final EquipmentService equipmentService;

    public AdminEquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("equipments", equipmentService.getAll());
        return "admin/equipments/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("equipment", new EquipmentRequest());
        model.addAttribute("editing", false);
        return "admin/equipments/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute EquipmentRequest request) {
        equipmentService.create(request);
        return "redirect:/admin/equipments";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("equipment", equipmentService.findById(id));
        model.addAttribute("editing", true);
        return "admin/equipments/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute EquipmentRequest request) {
        equipmentService.update(id, request);
        return "redirect:/admin/equipments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return "redirect:/admin/equipments";
    }
}
