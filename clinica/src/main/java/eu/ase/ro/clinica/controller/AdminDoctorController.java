package eu.ase.ro.clinica.controller;

import eu.ase.ro.clinica.dto.request.DoctorRequest;
import eu.ase.ro.clinica.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** CRUD complet pentru catalogul de medici (doar admin). */
@Controller
@RequestMapping("/admin/doctors")
public class AdminDoctorController {

    private final DoctorService doctorService;

    public AdminDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("doctors", doctorService.getAll());
        return "admin/doctors/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("doctor", new DoctorRequest());
        model.addAttribute("editing", false);
        return "admin/doctors/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DoctorRequest request) {
        doctorService.create(request);
        return "redirect:/admin/doctors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("doctor", doctorService.findById(id));
        model.addAttribute("editing", true);
        return "admin/doctors/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute DoctorRequest request) {
        doctorService.update(id, request);
        return "redirect:/admin/doctors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        doctorService.delete(id);
        return "redirect:/admin/doctors";
    }
}
