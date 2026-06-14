package eu.ase.ro.spring_mpai.response;

import java.util.List;

public class CourseResponse {
    private Long id;
    private String title;
    private String trainer;
    private String description;
    private List<StudentResponse> students;

    public CourseResponse() {
    }

    public CourseResponse(Long id, String title, String trainer, String description, List<StudentResponse> students) {
        this.id = id;
        this.title = title;
        this.trainer = trainer;
        this.description = description;
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StudentResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentResponse> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "CourseResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", trainer='" + trainer + '\'' +
                ", description='" + description + '\'' +
                ", students=" + students +
                '}';
    }
}
