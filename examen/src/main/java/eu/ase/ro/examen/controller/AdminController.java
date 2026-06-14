package eu.ase.ro.examen.controller;

import eu.ase.ro.examen.model.OrderStatus;
import eu.ase.ro.examen.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interfata administrator: vizualizeaza toate comenzile, filtreaza dupa client
 * si stare, si modifica starea unei comenzi (cu validarea tranzitiilor permise).
 */
@Controller
@RequestMapping("/admin/orders")
public class AdminController {

    private final OrderService orderService;

    public AdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String status,
                       @RequestParam(required = false) String clientName,
                       @RequestParam(required = false) String orderDate,
                       Model model) {
        model.addAttribute("orders", orderService.filter(status, clientName, orderDate));
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders/index";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders/detail";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String newStatus) {
        orderService.updateStatus(id, newStatus);
        return "redirect:/admin/orders/" + id;
    }
}
