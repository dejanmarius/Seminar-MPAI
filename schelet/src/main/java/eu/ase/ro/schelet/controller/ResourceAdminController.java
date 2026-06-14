package eu.ase.ro.schelet.controller;

import eu.ase.ro.schelet.dto.request.ResourceRequest;
import eu.ase.ro.schelet.service.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CRUD complet pentru catalogul de resurse, accesibil DOAR adminului.
 * Necesar in special pentru S3 (gestiunea echipamentelor) si S2 (medici).
 *
 * TODO: rename clasa + path "/admin/resources" -> "/admin/equipments" etc.
 */
@Controller
@RequestMapping("/admin/resources")
public class ResourceAdminController {

    private final ResourceService resourceService;

    public ResourceAdminController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    // Lista (GET /admin/resources)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("resources", resourceService.getAll());
        return "admin/resources/index";
    }

    // Form adaugare (GET /admin/resources/add)
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("resource", new ResourceRequest());
        model.addAttribute("editing", false);
        return "admin/resources/edit";
    }

    // Salvare adaugare (POST /admin/resources/save)
    @PostMapping("/save")
    public String save(@ModelAttribute ResourceRequest request) {
        resourceService.create(request);
        return "redirect:/admin/resources";
    }

    // Form editare (GET /admin/resources/1/edit)
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("resource", resourceService.findById(id));
        model.addAttribute("editing", true);
        return "admin/resources/edit";
    }

    // Salvare editare (POST /admin/resources/1/update)
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute ResourceRequest request) {
        resourceService.update(id, request);
        return "redirect:/admin/resources";
    }

    // Stergere (POST /admin/resources/1/delete)
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        resourceService.delete(id);
        return "redirect:/admin/resources";
    }
}
