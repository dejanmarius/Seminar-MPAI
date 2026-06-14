package eu.ase.ro.clinica.controller;

import eu.ase.ro.clinica.dto.request.AppointmentRequest;
import eu.ase.ro.clinica.service.AppointmentService;
import eu.ase.ro.clinica.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Interfata pacient: vede programarile, face o programare noua si anuleaza.
// NU are acces la schimbarea starilor — asta e doar pentru admin.
@Controller
@RequestMapping("/appointments") // toate rutele din acest controller incep cu /appointments
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    // GET /appointments — lista programarilor, cu filtru optional dupa numele pacientului
    // status=null si date=null -> pacientul nu filtreaza dupa stare sau data
    @GetMapping
    public String list(@RequestParam(required = false) String patientName, Model model) {
        model.addAttribute("appointments",
                appointmentService.filter(null, null, patientName, null));
        return "appointments/index"; // -> src/main/resources/templates/appointments/index.html
    }

    // GET /appointments/add — formular programare noua
    // trimitem lista medicilor in model ca sa poata fi afisata in dropdown
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("appointment", new AppointmentRequest());
        model.addAttribute("doctors", doctorService.getAll());
        return "appointments/edit"; // -> templates/appointments/edit.html
    }

    // POST /appointments/save — salveaza programarea si redirecteaza (PRG pattern)
    // @ModelAttribute leaga automat campurile din form pe obiectul AppointmentRequest
    @PostMapping("/save")
    public String save(@ModelAttribute AppointmentRequest request) {
        appointmentService.create(request);
        return "redirect:/appointments"; // PRG: evita retrimiterea formularului la refresh
    }

    // POST /appointments/{id}/cancel — anulare de catre pacient
    // service-ul valideaza starea si constrangerea de timp (minim 24h inainte)
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        appointmentService.cancelByPatient(id);
        return "redirect:/appointments";
    }
}
