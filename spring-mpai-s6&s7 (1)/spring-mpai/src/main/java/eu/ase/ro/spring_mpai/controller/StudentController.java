package eu.ase.ro.spring_mpai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/students")
    public String navigateToStudentsPage() {
        return "students/index";
    }

    @GetMapping("/students/add")
    public String navigateToAddStudentPage() {
        return "students/edit";
    }

    @GetMapping("/students/edit")
    public String navigateToEditStudentPage() {
        return "students/edit";
    }
}
