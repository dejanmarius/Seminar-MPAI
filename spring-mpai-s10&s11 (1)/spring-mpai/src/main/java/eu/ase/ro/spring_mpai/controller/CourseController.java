package eu.ase.ro.spring_mpai.controller;

import eu.ase.ro.spring_mpai.request.CourseRequest;
import eu.ase.ro.spring_mpai.response.CourseResponse;
import eu.ase.ro.spring_mpai.response.StudentResponse;
import eu.ase.ro.spring_mpai.service.CourseService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String navigateToCoursesPage(Model model) {
        List<CourseResponse> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        return "courses/index";
    }

    @GetMapping("/courses/{id}/edit")
    public String navigateToEditCoursePage(@PathVariable Long id, Model model) {
        CourseResponse response = courseService.findById(id);
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
        if(courseId == null){
            courseService.newCourse(request);
        }else{
            courseService.updateById(courseId, request);
        }
        return "redirect:/courses";

    }
}
