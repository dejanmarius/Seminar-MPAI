package eu.ase.ro.spring_mpai.request;

public class CourseRequest {

    private String title;
    private String trainer;
    private String description;

    public CourseRequest() {
    }

    public CourseRequest(String title, String trainer, String description) {
        this.title = title;
        this.trainer = trainer;
        this.description = description;
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

    @Override
    public String toString() {
        return "CourseRequest{" +
                "title='" + title + '\'' +
                ", trainer='" + trainer + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
