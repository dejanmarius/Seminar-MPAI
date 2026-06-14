package eu.ase.ro.schelet.controller;

import eu.ase.ro.schelet.dto.request.ItemRequest;
import eu.ase.ro.schelet.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Interfata client/utilizator: vede propriile inregistrari, creeaza una noua
 * si anuleaza una aflata inca in starea initiala. NU schimba stari (e doar admin).
 *
 * TODO: rename clasa -> OrderController / AppointmentController / RentalController
 * TODO: rename toate path-urile "/items" -> "/orders" / "/appointments" / "/rentals"
 */
@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Lista cu cautare dupa nume (GET /items?ownerName=Ion)
    @GetMapping
    public String list(@RequestParam(required = false) String ownerName, Model model) {
        model.addAttribute("items", itemService.filter(null, ownerName, null));
        return "items/index"; // -> templates/items/index.html
    }

    // Form creare noua (GET /items/add)
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new ItemRequest());
        return "items/edit";
    }

    // Salvare noua inregistrare (POST /items/save)
    @PostMapping("/save")
    public String save(@ModelAttribute ItemRequest request) {
        itemService.create(request);
        return "redirect:/items"; // PRG pattern - previne double-submit
    }

    // Anulare de catre utilizator (POST /items/1/cancel)
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        itemService.cancel(id);
        return "redirect:/items";
    }
}
