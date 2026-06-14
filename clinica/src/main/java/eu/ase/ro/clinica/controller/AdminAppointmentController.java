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

// Interfata admin/receptie: vede toate programarile, filtreaza si schimba stari.
@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // GET /admin/appointments — lista cu toate programarile, toate filtrele optionale
    // parametrii cu required=false sunt null daca nu sunt trimisi din form
    @GetMapping
    public String list(@RequestParam(required = false) String status,
                       @RequestParam(required = false) String doctorName,
                       @RequestParam(required = false) String patientName,
                       @RequestParam(required = false) String date,
                       Model model) {
        model.addAttribute("appointments",
                appointmentService.filter(status, doctorName, patientName, date));
        // trimitem toate valorile enum-ului pentru dropdown-ul de filtrare dupa stare
        model.addAttribute("statuses", AppointmentStatus.values());
        return "admin/appointments/index";
    }

    // GET /admin/appointments/{id} — detalii programare + form schimbare stare
    // trimitem si starile posibile pentru dropdown-ul de selectie stare noua
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", appointmentService.findById(id));
        model.addAttribute("statuses", AppointmentStatus.values());
        return "admin/appointments/detail";
    }

    // POST /admin/appointments/{id}/status — schimba starea
    // service-ul valideaza ca tranzitia e permisa conform nextStates()
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String newStatus) {
        appointmentService.updateStatus(id, newStatus);
        return "redirect:/admin/appointments/" + id; // inapoi la detalii dupa modificare
    }
}
