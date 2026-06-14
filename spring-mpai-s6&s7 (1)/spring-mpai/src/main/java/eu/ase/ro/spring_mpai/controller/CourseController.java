package eu.ase.ro.spring_mpai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseController {

    @GetMapping("/courses")
    public String navigateToCoursesPage() {
        return "courses/index";
    }

    @GetMapping("/courses/edit")
    public String navigateToEditCoursePage() {
        return "courses/edit";
    }

    @GetMapping("/courses/add")
    public String navigateToAddCoursePage() {
        return "courses/edit";
    }

    @GetMapping("/courses/students/enroll")
    public String navigateToEnrollStudentPage() {
        return "courses/enroll";
    }
}
