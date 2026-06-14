package eu.ase.ro.examen.controller;

import eu.ase.ro.examen.dto.request.OrderRequest;
import eu.ase.ro.examen.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Interfata client: vizualizarea propriilor comenzi, plasarea unei comenzi noi
 * si anularea unei comenzi aflate inca in starea initiala.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String clientName, Model model) {
        model.addAttribute("orders", orderService.filter(null, clientName, null));
        return "orders/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("order", new OrderRequest());
        return "orders/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute OrderRequest request) {
        orderService.create(request);
        return "redirect:/orders";
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return "redirect:/orders";
    }
}
