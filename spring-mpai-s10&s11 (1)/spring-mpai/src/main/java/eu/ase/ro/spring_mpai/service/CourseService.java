package eu.ase.ro.spring_mpai.service;

import eu.ase.ro.spring_mpai.model.Course;
import eu.ase.ro.spring_mpai.model.Student;
import eu.ase.ro.spring_mpai.repository.CourseRepository;
import eu.ase.ro.spring_mpai.request.CourseRequest;
import eu.ase.ro.spring_mpai.response.CourseResponse;
import eu.ase.ro.spring_mpai.response.StudentResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @PostConstruct
    private void init() {
        List<Course> courses = List.of(
                new Course("MPAI - Spring MVC", "Mr T", "totul despre modelul MVC implementat cu framekwork-ul Spring",
                        List.of(new Student("Popescu Ion", 23))),
                new Course("Java fundamentals", "Mr T", "hai sa invatam Java", List.of(new Student("Ionescu Maria", 22),
                        new Student("Voicu Daniel", 22)))
        );
        courseRepository.saveAll(courses);
    }

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void newCourse(CourseRequest request) {
        Course course = new Course(request.getTitle(), request.getTrainer(), request.getDescription());
        courseRepository.save(course);
    }

    public void updateById(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("course not found"));
        course.setName(request.getTitle());
        course.setTrainer(request.getTrainer());
        course.setDescription(request.getDescription());

        courseRepository.save(course);
    }

    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream()
                .map(this::toCourseResponse)
                .toList();
    }

    public CourseResponse findById(Long id) {
        return courseRepository.findById(id)
                .map(this::toCourseResponse)
                .orElseThrow(() -> new RuntimeException("course not found"));
    }

    private CourseResponse toCourseResponse(Course course) {
        Long id = course.getId();
        String name = course.getName();
        String trainer = course.getTrainer();
        String description = course.getDescription();
        List<StudentResponse> students = course.getStudents() == null
                ? List.of()
                : course.getStudents().stream()
                .map(this::toStudentResponse)
                .toList();
        return new CourseResponse(id, name, trainer, description, students);
    }

    private StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getAge());
    }
}
