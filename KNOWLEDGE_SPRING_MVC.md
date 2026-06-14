# Spring MVC - Knowledge Base (Seminarii S6-S11)

## Cuprins
1. [Structura proiectelor](#structura-proiectelor)
2. [Configurare & dependențe](#configurare--dependente)
3. [Adnotări esențiale](#adnotari-esentiale)
4. [S6-S7: Controllers și Templates](#s6-s7-controllers-si-templates)
5. [S8-S9: Data Binding și DTOs](#s8-s9-data-binding-si-dtos)
6. [S10-S11: Arhitectură completă cu JPA](#s10-s11-arhitectura-completa-cu-jpa)
7. [Thymeleaf - sintaxă esențială](#thymeleaf---sintaxa-esentiala)
8. [Endpoints aplicație](#endpoints-aplicatie)
9. [Evoluție comparativă](#evolutie-comparativa)
10. [Patterns & Best Practices](#patterns--best-practices)

---

## Structura proiectelor

Toate 3 seminarii folosesc același domeniu (cursuri și studenți) și aceeași structură de directoare:

```
spring-mpai/
├── pom.xml
└── src/main/
    ├── java/eu/ase/ro/spring_mpai/
    │   ├── SpringMpaiApplication.java     (@SpringBootApplication)
    │   ├── controller/
    │   │   ├── HomeController.java
    │   │   ├── CourseController.java
    │   │   └── StudentController.java
    │   ├── model/                         (S10-S11 only: entități JPA)
    │   │   ├── Course.java
    │   │   └── Student.java
    │   ├── service/                       (S10-S11 only)
    │   │   └── CourseService.java
    │   ├── repository/                    (S10-S11 only)
    │   │   ├── CourseRepository.java
    │   │   └── StudentRepository.java
    │   ├── request/                       (S8-S9 onwards)
    │   │   └── CourseRequest.java
    │   └── response/                      (S8-S9 onwards)
    │       ├── CourseResponse.java
    │       └── StudentResponse.java
    └── resources/
        ├── application.yaml
        ├── static/
        └── templates/
            ├── index.html
            ├── courses/
            │   ├── index.html
            │   ├── edit.html
            │   └── enroll.html
            └── students/
                ├── index.html
                └── edit.html
```

---

## Configurare & Dependente

### pom.xml - dependențe cheie

```xml
<!-- Spring MVC + Embedded Tomcat -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Template engine -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- JPA (S10-S11) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Baza de date in-memory (S10-S11) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### application.yaml

```yaml
spring:
  application:
    name: spring-mpai
server:
  servlet:
    context-path: /mpai-spring-mvc   # Toate URL-urile sunt prefixate cu asta
```

URL-ul complet devine: `http://localhost:8080/mpai-spring-mvc/courses`

---

## Adnotari Esentiale

### Controllers
| Adnotare | Rol |
|----------|-----|
| `@Controller` | Marchează clasa ca Spring MVC controller; returnează view names |
| `@GetMapping("/path")` | Mapează HTTP GET pe metoda respectivă |
| `@PostMapping("/path")` | Mapează HTTP POST pe metoda respectivă |
| `@PathVariable` | Extrage variabile din URL: `/courses/{id}` → `@PathVariable Long id` |
| `@RequestParam` | Extrage query params sau form params din request |
| `@ModelAttribute` | Bindează automat câmpurile din form la un obiect DTO |

### Entități JPA (S10-S11)
| Adnotare | Rol |
|----------|-----|
| `@Entity` | Marchează clasa ca entitate JPA (mapată pe tabel DB) |
| `@Table(name = "...")` | Specifică explicit numele tabelului |
| `@Id` | Marchează câmpul ca primary key |
| `@GeneratedValue(strategy = GenerationType.IDENTITY)` | Auto-increment ID |
| `@ManyToMany` | Relație many-to-many între două entități |
| `@JoinTable` | Definește tabelul de joncțiune pentru @ManyToMany |
| `@JoinColumn` | Specifică coloana de FK în tabelul de joncțiune |

### Spring Beans
| Adnotare | Rol |
|----------|-----|
| `@Service` | Marchează clasa ca service Spring (business logic) |
| `@Repository` | Marchează interfața ca repository Spring |
| `@Autowired` | Injecție de dependență (preferabil prin constructor) |
| `@PostConstruct` | Metoda se execută după ce bean-ul e creat și dependențele injectate |

---

## S6-S7: Controllers si Templates

### Concept: mapping simplu, fără logică

Controllers returnează doar numele template-urilor. Datele sunt hardcoded în HTML.

```java
@Controller
public class CourseController {

    @GetMapping("/courses")
    public String navigateToCoursesPage() {
        return "courses/index";  // → src/main/resources/templates/courses/index.html
    }

    @GetMapping("/courses/{id}/edit")
    public String navigateToEditCoursePage() {
        return "courses/edit";
    }

    @GetMapping("/courses/add")
    public String navigateToAddCoursePage() {
        return "courses/edit";   // Același template pentru add și edit
    }
}
```

```java
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/home")
    public String home() { return "index"; }
}
```

### Template HTML de bază (fără date dinamice)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Courses</title></head>
<body>
    <h1>Cursuri</h1>
    <table>
        <tr>
            <td>1</td>
            <td>MPAI</td>
            <td>Dr. T</td>
        </tr>
        <!-- Date hardcoded, nu dinamice -->
    </table>
</body>
</html>
```

**Key takeaway S6-S7:** Spring Boot + Thymeleaf = când returnezi un String dintr-un @Controller, Spring caută fișierul HTML în `templates/`.

---

## S8-S9: Data Binding si DTOs

### Concept: DTOs, Model, @PostConstruct, redirect

#### DTOs (Data Transfer Objects)

**CourseRequest** - preia date din form (POST):
```java
public class CourseRequest {
    private String title;
    private String trainer;
    private String description;
    // getters, setters, toString()
}
```

**CourseResponse** - trimite date spre view (GET):
```java
public class CourseResponse {
    private Long id;
    private String title;
    private String trainer;
    private String description;
    private List<StudentResponse> students;

    // Constructor fără argumente (necesar pentru @ModelAttribute)
    public CourseResponse() {}

    public CourseResponse(Long id, String title, String trainer,
                          String description, List<StudentResponse> students) { ... }
    // getters, setters, toString()
}
```

**StudentResponse**:
```java
public class StudentResponse {
    private Long id;
    private String name;
    private Integer age;
    // constructori, getters, setters
}
```

#### Controller cu logică și date în memorie

```java
@Controller
public class CourseController {

    List<CourseResponse> courses = new ArrayList<>();  // "baza de date" în memorie

    @PostConstruct  // Se execută după ce Spring creează bean-ul
    public void init() {
        List<StudentResponse> students = new ArrayList<>();
        students.add(new StudentResponse(1L, "Mircea Maria", 24));
        students.add(new StudentResponse(2L, "Tiriac Ion", 22));

        courses.add(new CourseResponse(1L, "MPAI", "Dr. T", "Spring MVC", students));
        courses.add(new CourseResponse(2L, "MDAS", "Dr. X", "Ceva frumos", students));
    }

    @GetMapping("/courses")
    public String navigateToCoursesPage(Model model) {
        model.addAttribute("courses", courses);   // Trimite lista în template
        return "courses/index";
    }

    @GetMapping("/courses/{id}/edit")
    public String navigateToEditCoursePage(@PathVariable Long id, Model model) {
        CourseResponse course = courses.stream()
                .filter(c -> c.getId().equals(id))
                .findAny()
                .orElseThrow();
        model.addAttribute("course", course);
        return "courses/edit";
    }

    @GetMapping("/courses/add")
    public String navigateToAddCoursePage(Model model) {
        model.addAttribute("course", new CourseResponse());  // Obiect gol pentru form
        return "courses/edit";
    }

    @PostMapping("/courses/save")
    public String save(@ModelAttribute CourseRequest request,
                       @RequestParam(required = false) Long courseId) {
        // courseId == null → add nou
        // courseId != null → edit existent
        System.out.println("Saved: " + request);
        return "redirect:/home";  // PRG pattern: redirect după POST
    }
}
```

#### Template cu date dinamice (Thymeleaf)

```html
<!-- courses/index.html -->
<table>
    <tr th:each="course : ${courses}">
        <td th:text="${course.id}">1</td>
        <td th:text="${course.title}">Demo</td>
        <td th:text="${course.trainer}">Demo</td>
        <td>
            <ul>
                <li th:each="student : ${course.students}">
                    <span th:text="${student.name}">Student</span>
                </li>
            </ul>
        </td>
        <td>
            <form th:action="@{'/courses/'+${course.id}+'/edit'}" method="get">
                <button type="submit">Edit</button>
            </form>
        </td>
    </tr>
</table>
```

```html
<!-- courses/edit.html -->
<form th:action="@{/courses/save}" method="post">
    <!-- Hidden field pentru a diferenția add vs edit -->
    <input type="hidden" name="courseId" th:value="${course != null ? course.id : null}"/>

    <input name="title" type="text" th:value="${course != null ? course.title : ''}"/>
    <input name="trainer" type="text" th:value="${course.trainer}"/>
    <input name="description" type="text" th:value="${course.description}"/>

    <button type="submit">Save</button>
</form>
```

**Key takeaways S8-S9:**
- `Model model` în parametrul metodei → poți adăuga atribute trimise în template
- `model.addAttribute("key", value)` → accesibil ca `${key}` în Thymeleaf
- `@ModelAttribute` pe parametrul POST → Spring bindează automat câmpurile form-ului la DTO
- `return "redirect:/path"` → PRG (Post/Redirect/Get) pattern, previne double-submit
- `@PostConstruct` → inițializare date după crearea bean-ului

---

## S10-S11: Arhitectura Completa cu JPA

### Concept: Entity, Repository, Service, Controller în straturi

#### Entități JPA

**Student.java:**
```java
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "course_students",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    // Constructor fără argumente (OBLIGATORIU pentru JPA)
    public Student() {}

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // getters, setters
}
```

**Course.java:**
```java
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String trainer;
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "course_students",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    public Course() {}

    public Course(String name, String trainer, String description) {
        this.name = name;
        this.trainer = trainer;
        this.description = description;
    }

    public Course(String name, String trainer, String description, List<Student> students) {
        this.name = name;
        this.trainer = trainer;
        this.description = description;
        this.students = students;
    }

    // getters, setters
}
```

**Relația @ManyToMany explicată:**
- Tabel de joncțiune `course_students` cu coloanele `course_id` și `student_id`
- `CascadeType.PERSIST` → când salvezi un Course, se salvează și studenții asociați
- `FetchType.LAZY` → studenții nu se încarcă automat; se încarcă doar când accesezi `.getStudents()`

#### Repositories

```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // JpaRepository<TipEntitate, TipId>
    // Moștenește: save(), findById(), findAll(), delete(), deleteById(), saveAll()
}

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Poți adăuga query-uri custom:
    // List<Course> findByTrainer(String trainer);
    // List<Course> findByNameContaining(String keyword);
}
```

#### Service Layer

```java
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired  // Injecție prin constructor (best practice)
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PostConstruct
    private void init() {
        List<Course> courses = List.of(
            new Course("MPAI - Spring MVC", "Mr T",
                       "Spring MVC framework",
                       List.of(new Student("Popescu Ion", 23))),
            new Course("Java Fundamentals", "Mr T",
                       "Bazele Java",
                       List.of(new Student("Ionescu Maria", 22),
                               new Student("Voicu Daniel", 22)))
        );
        courseRepository.saveAll(courses);
    }

    // CREATE
    public void newCourse(CourseRequest request) {
        Course course = new Course(request.getTitle(),
                                   request.getTrainer(),
                                   request.getDescription());
        courseRepository.save(course);
    }

    // UPDATE
    public void updateById(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setName(request.getTitle());
        course.setTrainer(request.getTrainer());
        course.setDescription(request.getDescription());
        courseRepository.save(course);
    }

    // READ ALL
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream()
                .map(this::toCourseResponse)
                .toList();
    }

    // READ BY ID
    public CourseResponse findById(Long id) {
        return courseRepository.findById(id)
                .map(this::toCourseResponse)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // Mapper privat: Entity → DTO
    private CourseResponse toCourseResponse(Course course) {
        List<StudentResponse> students = course.getStudents() == null
                ? List.of()
                : course.getStudents().stream()
                    .map(s -> new StudentResponse(s.getId(), s.getName(), s.getAge()))
                    .toList();
        return new CourseResponse(course.getId(), course.getName(),
                                  course.getTrainer(), course.getDescription(), students);
    }
}
```

#### Controller delegă la Service

```java
@Controller
public class CourseController {

    private final CourseService courseService;

    // Injecție prin constructor (fără @Autowired explicit în Spring Boot modern)
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
        CourseResponse course = courseService.findById(id);
        model.addAttribute("course", course);
        return "courses/edit";
    }

    @GetMapping("/courses/add")
    public String navigateToAddCoursePage(Model model) {
        model.addAttribute("course", new CourseResponse());
        return "courses/edit";
    }

    @PostMapping("/courses/save")
    public String save(@ModelAttribute CourseRequest request,
                       @RequestParam(required = false) Long courseId) {
        if (courseId == null) {
            courseService.newCourse(request);    // CREATE
        } else {
            courseService.updateById(courseId, request);  // UPDATE
        }
        return "redirect:/courses";
    }
}
```

**Key takeaways S10-S11:**
- Controller nu mai ține date; delegă complet la Service
- Service face mapping Entity ↔ DTO
- Repository e interfață, Spring generează implementarea automat
- `JpaRepository<T, ID>` dă CRUD gratuit: `save`, `findAll`, `findById`, `delete`
- `orElseThrow()` → aruncă excepție dacă entitatea nu există
- Constructor fără argumente este OBLIGATORIU pe entitățile JPA

---

## Thymeleaf - Sintaxa Esentiala

### Expresii de bază

```html
<!-- Namespace obligatoriu pe tag-ul html -->
<html xmlns:th="http://www.thymeleaf.org">

<!-- Afișare text -->
<td th:text="${course.title}">Fallback text</td>

<!-- Iterație -->
<tr th:each="course : ${courses}">
    <td th:text="${course.id}"></td>
</tr>

<!-- URL dinamic -->
<a th:href="@{/courses}">Cursuri</a>
<a th:href="@{'/courses/' + ${course.id} + '/edit'}">Edit</a>
<form th:action="@{/courses/save}" method="post">

<!-- Valoare câmp form -->
<input th:value="${course.title}" name="title" type="text"/>

<!-- Operator ternar -->
<input th:value="${course != null ? course.id : null}" type="hidden"/>
<input th:value="${course != null ? course.title : ''}" type="text"/>

<!-- Atribut dinamic -->
<img th:src="@{/images/logo.png}"/>
```

### Pattern form add/edit (cu hidden field)

```html
<form th:action="@{/courses/save}" method="post">
    <!-- null pentru add, ID-ul pentru edit -->
    <input type="hidden" name="courseId" th:value="${course != null ? course.id : null}"/>

    <input name="title" type="text" th:value="${course.title}"/>
    <input name="trainer" type="text" th:value="${course.trainer}"/>
    <input name="description" type="text" th:value="${course.description}"/>

    <button type="submit">Save</button>
</form>
```

### Iterație cu nested list

```html
<tr th:each="course : ${courses}">
    <td th:text="${course.title}"></td>
    <td>
        <ul>
            <li th:each="student : ${course.students}">
                <span th:text="${student.name}"></span>
                (<span th:text="${student.age}"></span> ani)
            </li>
        </ul>
    </td>
</tr>
```

---

## Endpoints Aplicatie

Context path: `/mpai-spring-mvc` (prefix la toate URL-urile)

| Method | Path | Action | Template |
|--------|------|--------|----------|
| GET | `/` | Home | `index.html` |
| GET | `/home` | Home | `index.html` |
| GET | `/courses` | Lista cursuri | `courses/index.html` |
| GET | `/courses/add` | Form adăugare curs | `courses/edit.html` |
| GET | `/courses/{id}/edit` | Form editare curs | `courses/edit.html` |
| POST | `/courses/save` | Salvare curs (add/edit) | redirect → `/courses` |
| GET | `/courses/students/enroll` | Form înscrierea studenți | `courses/enroll.html` |
| GET | `/students` | Lista studenți | `students/index.html` |
| GET | `/students/add` | Form adăugare student | `students/edit.html` |
| GET | `/students/edit` | Form editare student | `students/edit.html` |

---

## Evolutie Comparativa

| Aspect | S6-S7 | S8-S9 | S10-S11 |
|--------|-------|-------|---------|
| Controllers | Returnează view name | Procesează date, Model | Delegă la Service |
| Date în template | Hardcoded | Dinamice (din `Model`) | Dinamice (din Service) |
| DTO-uri Request/Response | Nu | Da | Da |
| Entități JPA | Nu | Nu | Da |
| Repository | Nu | Nu | Da (`JpaRepository`) |
| Service Layer | Nu | Nu | Da |
| Inițializare date | N/A | `@PostConstruct` în controller | `@PostConstruct` în service |
| Persistență | N/A | Listă în memorie (RAM) | H2 Database |
| Thymeleaf loops | Nu | Da (`th:each`) | Da (`th:each`) |
| Relații DB | Nu | Nu | `@ManyToMany` |
| Mapping Entity→DTO | N/A | N/A | Mapper privat în Service |
| CRUD | Navigare | Read + redirect | Create + Read + Update |

---

## Patterns & Best Practices

### 1. PRG Pattern (Post/Redirect/Get)
Returnezi `"redirect:/path"` după un POST pentru a preveni retrimiterea formularului la refresh.
```java
@PostMapping("/courses/save")
public String save(...) {
    // procesează...
    return "redirect:/courses";  // Nu "courses/index"!
}
```

### 2. Același template pentru Add și Edit
```java
@GetMapping("/courses/add")
public String add(Model model) {
    model.addAttribute("course", new CourseResponse());  // obiect gol
    return "courses/edit";
}

@GetMapping("/courses/{id}/edit")
public String edit(@PathVariable Long id, Model model) {
    model.addAttribute("course", courseService.findById(id));  // obiect populat
    return "courses/edit";
}
```
Template-ul distinge cu `th:value="${course != null ? course.id : null}"`.

### 3. Injecție prin constructor (preferred)
```java
// Good - injecție prin constructor
@Controller
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
}

// Alternativă (dar mai puțin testabilă)
@Autowired
private CourseService courseService;
```

### 4. Separarea layerelor (SoC - Separation of Concerns)
- **Controller** → Primește request, returnează view name sau redirect
- **Service** → Business logic, orchestrare, mapping Entity↔DTO
- **Repository** → Acces baza de date, query-uri
- **Entity** → Structura tabelelor DB
- **DTO** → Structura datelor transferate între layere

### 5. Mapping Entity → DTO în Service
Nu expune entitățile JPA direct în view. Mapează-le la DTO-uri:
```java
private CourseResponse toCourseResponse(Course course) {
    return new CourseResponse(
        course.getId(),
        course.getName(),
        course.getTrainer(),
        course.getDescription(),
        course.getStudents().stream()
            .map(s -> new StudentResponse(s.getId(), s.getName(), s.getAge()))
            .toList()
    );
}
```

### 6. orElseThrow() pentru entități care nu există
```java
Course course = courseRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
```

### 7. JpaRepository - metode disponibile gratuit
```java
repository.save(entity)          // INSERT sau UPDATE
repository.saveAll(list)         // Salvează mai multe
repository.findById(id)          // Returnează Optional<T>
repository.findAll()             // Returnează List<T>
repository.deleteById(id)        // DELETE by id
repository.delete(entity)        // DELETE by entity
repository.count()               // Număr de înregistrări
repository.existsById(id)        // Verifică existența
```

---

## Flux complet cerere HTTP

```
Browser → GET /mpai-spring-mvc/courses
    ↓
DispatcherServlet (Spring MVC)
    ↓
CourseController.navigateToCoursesPage(Model model)
    ↓
courseService.getAll()
    ↓
courseRepository.findAll() → [Course, Course, ...]
    ↓
map la List<CourseResponse>
    ↓
model.addAttribute("courses", courses)
    ↓
return "courses/index"
    ↓
Thymeleaf renders courses/index.html cu ${courses}
    ↓
Browser primește HTML
```

---

## Erori comune de evitat

1. **Lipsă constructor fără argumente pe Entity** → `HibernateException`
2. **Lipsă `name` pe input în form** → `@ModelAttribute` nu poate binda câmpul
3. **`return "courses/index"` după POST** → dublare submit la refresh (folosește `redirect:`)
4. **`th:action` fără `@{}`** → URL-ul nu se construiește corect cu context path
5. **Acces `getStudents()` pe un entity cu `FetchType.LAZY` în afara tranzacției** → `LazyInitializationException`
6. **Circularitate `@ManyToMany` fără `mappedBy`** → poate crea 2 tabele de joncțiune
