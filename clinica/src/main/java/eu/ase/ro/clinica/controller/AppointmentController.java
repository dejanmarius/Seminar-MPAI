package eu.ase.ro.clinica.controller;

import eu.ase.ro.clinica.dto.request.AppointmentRequest;
import eu.ase.ro.clinica.service.AppointmentService;
import eu.ase.ro.clinica.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Interfata pacient: vede programarile, face o programare noua (alege medicul)
 * si anuleaza daca starea + constrangerea de timp permit. NU schimba stari.
 */
@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String patientName, Model model) {
        model.addAttribute("appointments",
                appointmentService.filter(null, null, patientName, null));
        return "appointments/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("appointment", new AppointmentRequest());
        model.addAttribute("doctors", doctorService.getAll()); // pentru dropdown medic
        return "appointments/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AppointmentRequest request) {
        appointmentService.create(request);
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        appointmentService.cancelByPatient(id);
        return "redirect:/appointments";
    }
}
