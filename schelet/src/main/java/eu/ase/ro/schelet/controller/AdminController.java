package eu.ase.ro.schelet.controller;

import eu.ase.ro.schelet.model.ItemStatus;
import eu.ase.ro.schelet.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interfata administrator: vede toate inregistrarile, filtreaza dupa nume,
 * stare si data, si modifica starea (cu validarea tranzitiilor permise).
 *
 * TODO: rename clasa -> AdminOrderController / AdminAppointmentController etc.
 * TODO: rename path-ul "/admin/items" -> "/admin/orders" etc.
 */
@Controller
@RequestMapping("/admin/items")
public class AdminController {

    private final ItemService itemService;

    public AdminController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Lista completa cu filtrare dupa nume + stare + data (GET /admin/items)
    @GetMapping
    public String list(@RequestParam(required = false) String status,
                       @RequestParam(required = false) String ownerName,
                       @RequestParam(required = false) String date,
                       Model model) {
        model.addAttribute("items", itemService.filter(status, ownerName, date));
        model.addAttribute("statuses", ItemStatus.values());
        return "admin/items/index"; // -> templates/admin/items/index.html
    }

    // Detalii + schimbare stare (GET /admin/items/1)
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("item", itemService.findById(id));
        model.addAttribute("statuses", ItemStatus.values());
        return "admin/items/detail";
    }

    // Schimbare stare (POST /admin/items/1/status)
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String newStatus) {
        itemService.updateStatus(id, newStatus);
        return "redirect:/admin/items/" + id;
    }
}
