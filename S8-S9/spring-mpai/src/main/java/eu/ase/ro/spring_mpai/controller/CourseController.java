package eu.ase.ro.spring_mpai.controller;

import eu.ase.ro.spring_mpai.request.CourseRequest;
import eu.ase.ro.spring_mpai.response.CourseResponse;
import eu.ase.ro.spring_mpai.response.StudentResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {

    List<CourseResponse> courses = new ArrayList<>();

    @PostConstruct
    public void init(){
        List<StudentResponse> students = new ArrayList<>();
        students.add(new StudentResponse(1L, "Mirceea Maria", 24));
        students.add(new StudentResponse(2L, "Tiriac Ion IV", 22));
        CourseResponse course1 = new CourseResponse(1L, "MPAI", "Dr. T", "Invatam speranta", students);
        CourseResponse course2 = new CourseResponse(2L, "MDAS", "Dr. X", "Ceevaa frumos", students);
        courses.add(course1);
        courses.add(course2);

        System.out.println("courses has been initialized");
    }

    @GetMapping("/courses")
    public String navigateToCoursesPage(Model model) {

        model.addAttribute("courses", courses);
        return "courses/index";
    }

    @GetMapping("/courses/{id}/edit")
    public String navigateToEditCoursePage(@PathVariable Long id, Model model) {
        CourseResponse response = courses.stream()
                .filter(c -> c.getId().equals(id))
                .findAny()
                .orElseThrow();
        model.addAttribute("course", response);

        return "courses/edit";
    }

    @GetMapping("/courses/add")
    public String navigateToAddCoursePage(Model model) {
        model.addAttribute("course", new CourseResponse());
        return "courses/edit";
    }

    @GetMapping("/courses/students/enroll")
    public String navigateToEnrollStudentPage() {
        return "courses/enroll";
    }

    @PostMapping("/courses/save")
    public String save(@ModelAttribute CourseRequest request,
                       @RequestParam(required = false) Long courseId){
        System.out.println("Course Id received: "+courseId);
        System.out.println("Course has been saved: "+request.toString());
        return "redirect:/home";

    }
}
