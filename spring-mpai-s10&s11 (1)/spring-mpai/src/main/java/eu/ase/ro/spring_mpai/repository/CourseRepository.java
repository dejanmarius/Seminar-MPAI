package eu.ase.ro.spring_mpai.repository;

import eu.ase.ro.spring_mpai.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
