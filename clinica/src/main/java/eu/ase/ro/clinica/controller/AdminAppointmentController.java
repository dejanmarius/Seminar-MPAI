package eu.ase.ro.clinica.controller;

import eu.ase.ro.clinica.model.AppointmentStatus;
import eu.ase.ro.clinica.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interfata admin/receptie: vede toate programarile, filtreaza dupa
 * stare/medic/pacient/data, confirma/anuleaza si marcheaza ca efectuata.
 */
@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String status,
                       @RequestParam(required = false) String doctorName,
                       @RequestParam(required = false) String patientName,
                       @RequestParam(required = false) String date,
                       Model model) {
        model.addAttribute("appointments",
                appointmentService.filter(status, doctorName, patientName, date));
        model.addAttribute("statuses", AppointmentStatus.values());
        return "admin/appointments/index";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", appointmentService.findById(id));
        model.addAttribute("statuses", AppointmentStatus.values());
        return "admin/appointments/detail";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String newStatus) {
        appointmentService.updateStatus(id, newStatus);
        return "redirect:/admin/appointments/" + id;
    }
}
