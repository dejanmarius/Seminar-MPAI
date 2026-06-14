# Analiza Subiecte Examen Spring MVC

## Cuprins
1. [Subiect 1 - Magazin Virtual](#subiect-1---magazin-virtual)
2. [Subiect 2 - Clinică (Programări)](#subiect-2---clinica-programari)
3. [Subiect 3 - Închirieri Echipamente](#subiect-3---inchirieri-echipamente)
4. [Analiză Comparativă](#analiza-comparativa)
5. [Schelet Universal de Soluție](#schelet-universal-de-solutie)

---

## Subiect 1 - Magazin Virtual

**Domeniu:** Gestiunea comenzilor într-un magazin virtual

### Descriere
Aplicație pentru gestionarea comenzilor plasate de clienți. O comandă poate trece prin stările: **plasată → procesată → expediată → livrată**.

### Actori
| Actor | Permisiuni |
|-------|-----------|
| **Client** | Urmărește starea comenzilor; primește notificări la fiecare actualizare |
| **Administrator** | Vizualizează comenzile; filtrează/caută după: dată, client, stare; modifică starea comenzilor |

### Funcționalități detaliate
- **Clienți:**
  - Pot urmări starea comenzilor
  - Primesc notificări la fiecare schimbare de stare
  - Posibilitate de anulare a comenzilor înainte de procesare
- **Administratori:**
  - Pot vizualiza toate comenzile
  - Filtrare/căutare după: dată, client, stare
  - Modificarea stării comenzilor
- **Notificări:** trimise clienților la fiecare actualizare a stării comenzii

### Stări posibile
```
PLASATA → PROCESATA → EXPEDIATA → LIVRATA
             ↑
         ANULATA (doar înainte de PROCESATA)
```

### Entități principale
- `Order` (comandă): id, client, data, stare, produse
- `Client`: id, nume, email
- `Product` (opțional): id, nume, preț

### Cerințe tehnice (comune tuturor subiectelor)
1. Spring MVC (Model View Controller)
2. Thymeleaf template engine
3. H2 in-memory database
4. Spring Data JPA
5. Populare automată la pornire cu **minim 5 înregistrări** din fișier text
6. DTO-uri obligatorii — **modelele NU se folosesc direct în View-uri**

---

## Subiect 2 - Clinica (Programari)

**Domeniu:** Gestiunea programărilor într-o clinică

### Descriere
Aplicație pentru gestionarea programărilor făcute de pacienți la medici. O programare poate avea stările: **solicitată → confirmată → efectuată / anulată**.

### Actori
| Actor | Permisiuni |
|-------|-----------|
| **Pacient** | Vede lista programărilor și starea; face programare; anulează dacă e în starea *solicitată* sau *confirmată* (cu minim X ore înainte) |
| **Administrator/Recepție** | Vede toate programările; filtrează/caută; confirmă sau anulează; marchează ca efectuată |

### Funcționalități detaliate
- **Pacienți:**
  - Văd lista programărilor proprii și starea fiecăreia
  - Pot face o programare: aleg medic, dată/oră, motiv scurt
  - Pot **anula** programarea doar dacă e în starea **solicitată** sau **confirmată** (cu minim X ore înainte)
- **Administratori/Recepție:**
  - Vizualizează toate programările
  - Filtrare/căutare după: dată/interval, medic, pacient, stare
  - Pot **confirma** sau **anula** programări
  - Pot marca o programare ca **efectuată**
- **Notificări:** pacientul primește notificare la fiecare schimbare de stare

### Stări posibile
```
SOLICITATA → CONFIRMATA → EFECTUATA
     ↓              ↓
  ANULATA       ANULATA
(cu X ore înainte) (cu X ore înainte)
```

### Entități principale
- `Appointment` (programare): id, pacient, medic, data, ora, motiv, stare
- `Patient` (pacient): id, nume, email, telefon
- `Doctor` (medic): id, nume, specializare

### Specificitate față de celelalte
- Constrângere temporală la anulare (minim X ore înainte)
- Doi actori de admin (Administrator + Recepție)
- Câmp obligatoriu `motiv` la creare programare

---

## Subiect 3 - Inchirieri Echipamente

**Domeniu:** Gestiunea închirierilor de echipamente (laborator, sală sport, bibliotecă de unelte)

### Descriere
Aplicație pentru gestionarea cererilor de închiriere. O închiriere poate avea stările: **cerută → aprobată → preluată → returnată / respinsă**.

### Actori
| Actor | Permisiuni |
|-------|-----------|
| **Utilizator** | Vede cererile/închirierile proprii și stările; face cerere de închiriere; anulează înainte de aprobare |
| **Administrator** | Gestionează lista de echipamente; vede toate cererile; filtrează/caută; aprobă/respinge; marchează ca preluată/returnată |

### Funcționalități detaliate
- **Utilizatori:**
  - Văd cererile/închirierile proprii și stările
  - Pot face o cerere de închiriere: echipament, perioadă, scop
  - Pot **anula** cererea înainte să fie aprobată
- **Administratori:**
  - Gestionează lista de echipamente (minim: nume, descriere, disponibilitate)
  - Vizualizează toate cererile de închiriere
  - Filtrare/căutare după: dată, utilizator, echipament, stare
  - Pot **aproba** sau **respinge** cereri
  - Pot marca o cerere ca **"preluată"** și **"returnată"**
- **Notificări:** utilizatorii primesc notificare la fiecare schimbare de stare

### Stări posibile
```
CERUTA → APROBATA → PRELUATA → RETURNATA
    ↓         ↑
ANULATA   RESPINSA
(înainte de aprobare)
```

### Entități principale
- `RentalRequest` (cerere): id, utilizator, echipament, perioadă start, perioadă end, scop, stare
- `User` (utilizator): id, nume, email
- `Equipment` (echipament): id, nume, descriere, disponibilitate (boolean)

### Specificitate față de celelalte
- Entitate separată `Equipment` gestionată de admin (CRUD echipamente)
- Câmp `disponibilitate` pe echipament — se actualizează la aprobare/returnare
- Perioadă (start + end date) în loc de o singură dată/oră
- Câmp `scop` la creare cerere

---

## Analiza Comparativa

### Similitudini (toate 3 subiecte)

| Aspect | S1 Magazin | S2 Clinică | S3 Echipamente |
|--------|-----------|-----------|---------------|
| Tehnologii | Spring MVC, Thymeleaf, H2, JPA | idem | idem |
| Cerințe | 1-6 identice | 1-6 identice | 1-6 identice |
| Stări entitate | 4-5 stări | 4 stări | 5 stări |
| 2 tipuri actori | Client + Admin | Pacient + Admin | User + Admin |
| Notificări | Da | Da | Da |
| Filtrare/căutare | Da | Da | Da |
| Init. din fișier | 5+ înregistrări | 5+ înregistrări | 5+ înregistrări |
| DTO obligatoriu | Da | Da | Da |

### Diferențe cheie

| Aspect | S1 Magazin | S2 Clinică | S3 Echipamente |
|--------|-----------|-----------|---------------|
| Entități | Order, Client | Appointment, Patient, Doctor | RentalRequest, User, Equipment |
| Entitate extra | - | Doctor (gestionat?) | Equipment (CRUD complet admin) |
| Constrângere specială | Anulare doar înainte de procesare | Anulare cu X ore înainte | Anulare înainte de aprobare |
| Câmp special | - | motiv + dată/oră | scop + perioadă (start/end) |
| Complexitate | Medie | Medie-Mare | Mare |

### Pattern de stări (comun)

Toate subiectele au același pattern:
- **Stare inițială** → poate fi anulată de utilizator
- **Stări intermediare** → gestionate de admin
- **Stare finală** → ireversibilă
- **Notificare** la fiecare tranziție

### Grila de evaluare estimată

| Criteriu | Punctaj estimat |
|---------|----------------|
| Compilare + pornire aplicație | Obligatoriu |
| Structură MVC corectă | 1-2p |
| Thymeleaf + Pagini funcționale | 2-3p |
| H2 + JPA configurate | 1p |
| DTO-uri corecte | 1p |
| Init. din fișier text | 1p |
| CRUD funcțional | 2-3p |
| Tranzițiile de stare | 1-2p |
| Filtrare/căutare | 1p |
| Notificări | 1p |

---

## Schelet Universal de Solutie

Acest schelet se aplică la orice subiect de tip examen de mai sus. Înlocuiești numele domeniului și câmpurile specifice.

### 1. Structura proiectului

```
src/main/
├── java/eu/ase/ro/app/
│   ├── AppApplication.java
│   ├── controller/
│   │   ├── HomeController.java
│   │   ├── [EntityController].java      (ex: OrderController, AppointmentController)
│   │   └── AdminController.java
│   ├── model/
│   │   ├── [MainEntity].java            (ex: Order, Appointment, RentalRequest)
│   │   ├── [UserEntity].java            (ex: Client, Patient, User)
│   │   ├── [ExtraEntity].java           (ex: Doctor, Equipment — dacă există)
│   │   ├── [EntityStatus].java          (enum cu stările)
│   │   └── Notification.java
│   ├── repository/
│   │   ├── [MainEntity]Repository.java
│   │   ├── [UserEntity]Repository.java
│   │   └── NotificationRepository.java
│   ├── service/
│   │   ├── [MainEntity]Service.java
│   │   └── NotificationService.java
│   ├── request/
│   │   └── [MainEntity]Request.java
│   └── response/
│       ├── [MainEntity]Response.java
│       └── [UserEntity]Response.java
└── resources/
    ├── application.yaml
    ├── data/
    │   └── init-data.txt                (fișier cu date inițiale)
    └── templates/
        ├── index.html
        ├── [entity]/
        │   ├── index.html               (lista)
        │   ├── edit.html                (add/edit form)
        │   └── detail.html             (detalii + schimbare stare)
        └── admin/
            └── index.html              (panou admin)
```

### 2. pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 ...">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.1</version>
    </parent>
    <groupId>eu.ase.ro</groupId>
    <artifactId>app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### 3. application.yaml

```yaml
spring:
  application:
    name: app
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true          # Accesibil la /h2-console
  jpa:
    hibernate:
      ddl-auto: create-drop  # Creează tabelele la pornire, le șterge la oprire
    show-sql: true
server:
  servlet:
    context-path: /home      # Sau /mpai-spring-mvc
  port: 8080
```

> **Atenție:** `context-path: /home` face ca aplicația să fie accesibilă la `http://localhost:8080/home`.
> Dacă pui `context-path: /home`, atunci `/` și `/home` din HomeController vor fi accesibile la `http://localhost:8080/home/` respectiv `http://localhost:8080/home/home`.

### 4. Enum pentru stări

```java
// model/OrderStatus.java (sau AppointmentStatus, RentalStatus etc.)
public enum OrderStatus {
    PLASATA,
    PROCESATA,
    EXPEDIATA,
    LIVRATA,
    ANULATA;

    // Returnează stările la care poate tranziționa din starea curentă
    public List<OrderStatus> nextStates() {
        return switch (this) {
            case PLASATA -> List.of(PROCESATA, ANULATA);
            case PROCESATA -> List.of(EXPEDIATA);
            case EXPEDIATA -> List.of(LIVRATA);
            default -> List.of();
        };
    }

    public boolean canBeModifiedByClient() {
        return this == PLASATA;
    }
}
```

### 5. Entitate principală (Entity)

```java
// model/Order.java
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;
    private String clientEmail;

    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)   // Salvează enum ca String în DB
    private OrderStatus status;

    private String description;    // sau alte câmpuri specifice domeniului

    // Constructor fără argumente OBLIGATORIU pentru JPA
    public Order() {}

    public Order(String clientName, String clientEmail,
                 LocalDate orderDate, String description) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.orderDate = orderDate;
        this.description = description;
        this.status = OrderStatus.PLASATA;   // Starea inițială la creare
    }

    // getters, setters
}
```

### 6. Notification Entity

```java
// model/Notification.java
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientEmail;
    private String message;
    private LocalDateTime sentAt;
    private boolean read;

    public Notification() {}

    public Notification(String recipientEmail, String message) {
        this.recipientEmail = recipientEmail;
        this.message = message;
        this.sentAt = LocalDateTime.now();
        this.read = false;
    }

    // getters, setters
}
```

### 7. Repository

```java
// repository/OrderRepository.java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Spring Data generează automat SQL-ul pe baza numelui metodei:
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByClientEmail(String email);
    List<Order> findByClientNameContainingIgnoreCase(String name);
    List<Order> findByOrderDateBetween(LocalDate start, LocalDate end);

    // Query custom cu JPQL dacă e nevoie de ceva mai complex:
    @Query("SELECT o FROM Order o WHERE " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:clientName IS NULL OR LOWER(o.clientName) LIKE LOWER(CONCAT('%',:clientName,'%')))")
    List<Order> findByFilters(@Param("status") OrderStatus status,
                              @Param("clientName") String clientName);
}
```

### 8. DTOs

```java
// request/OrderRequest.java  (date din form → service)
public class OrderRequest {
    private String clientName;
    private String clientEmail;
    private String description;
    // getters, setters
}

// response/OrderResponse.java  (service → view)
public class OrderResponse {
    private Long id;
    private String clientName;
    private String clientEmail;
    private LocalDate orderDate;
    private String status;         // String, nu enum, pentru Thymeleaf
    private String description;
    private List<String> availableActions;  // Acțiuni posibile pentru UI

    public OrderResponse() {}

    // Constructor complet
    public OrderResponse(Long id, String clientName, String clientEmail,
                         LocalDate orderDate, String status, String description) {
        this.id = id;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.orderDate = orderDate;
        this.status = status;
        this.description = description;
    }
    // getters, setters
}
```

### 9. Service Layer

```java
// service/OrderService.java
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    // ===== INIȚIALIZARE DIN FIȘIER TEXT =====
    @PostConstruct
    public void init() {
        // Citire din fișier text din resources/data/init-data.txt
        try {
            InputStream is = getClass().getResourceAsStream("/data/init-data.txt");
            if (is == null) return;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                // Format linie: clientName,clientEmail,description
                Order order = new Order(parts[0].trim(), parts[1].trim(),
                                        LocalDate.now(), parts[2].trim());
                orderRepository.save(order);
            }
        } catch (Exception e) {
            System.err.println("Eroare la inițializare date: " + e.getMessage());
        }
    }

    // ===== CRUD =====

    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<OrderResponse> getByClientEmail(String email) {
        return orderRepository.findByClientEmail(email).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<OrderResponse> filter(String status, String clientName) {
        OrderStatus orderStatus = (status != null && !status.isBlank())
                ? OrderStatus.valueOf(status) : null;
        return orderRepository.findByFilters(orderStatus, clientName).stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse findById(Long id) {
        return orderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public void create(OrderRequest request) {
        Order order = new Order(request.getClientName(), request.getClientEmail(),
                                LocalDate.now(), request.getDescription());
        orderRepository.save(order);
        notificationService.send(order.getClientEmail(),
            "Comanda ta a fost plasată cu succes!");
    }

    // ===== TRANZIȚII DE STARE =====

    public void updateStatus(Long id, String newStatusStr) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));

        OrderStatus newStatus = OrderStatus.valueOf(newStatusStr);

        // Validare tranziție permisă
        if (!order.getStatus().nextStates().contains(newStatus)) {
            throw new RuntimeException("Tranziție nepermisă: "
                + order.getStatus() + " → " + newStatus);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        notificationService.send(order.getClientEmail(),
            "Starea comenzii tale a fost actualizată: " + newStatus.name());
    }

    public void cancel(Long id) {
        updateStatus(id, "ANULATA");
    }

    // ===== MAPPER Entity → DTO =====

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getClientName(),
            order.getClientEmail(),
            order.getOrderDate(),
            order.getStatus().name(),
            order.getDescription()
        );
    }
}
```

### 10. NotificationService

```java
// service/NotificationService.java
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void send(String recipientEmail, String message) {
        Notification notification = new Notification(recipientEmail, message);
        notificationRepository.save(notification);
        // În aplicație reală ar trimite email; aici doar salvăm în DB
        System.out.println("NOTIFICARE → " + recipientEmail + ": " + message);
    }

    public List<Notification> getUnread(String email) {
        return notificationRepository.findByRecipientEmailAndRead(email, false);
    }
}
```

### 11. Controller

```java
// controller/OrderController.java
@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ===== LIST (pentru client) =====
    @GetMapping("/orders")
    public String list(@RequestParam(required = false) String email, Model model) {
        List<OrderResponse> orders = (email != null && !email.isBlank())
                ? orderService.getByClientEmail(email)
                : orderService.getAll();
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "orders/index";
    }

    // ===== ADMIN - FILTRARE =====
    @GetMapping("/admin/orders")
    public String adminList(@RequestParam(required = false) String status,
                            @RequestParam(required = false) String clientName,
                            Model model) {
        model.addAttribute("orders", orderService.filter(status, clientName));
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders";
    }

    // ===== DETALII + ACȚIUNI =====
    @GetMapping("/orders/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("statuses", OrderStatus.values());
        return "orders/detail";
    }

    // ===== ADD FORM =====
    @GetMapping("/orders/add")
    public String addForm(Model model) {
        model.addAttribute("order", new OrderRequest());
        return "orders/edit";
    }

    // ===== SAVE (CREATE) =====
    @PostMapping("/orders/save")
    public String save(@ModelAttribute OrderRequest request) {
        orderService.create(request);
        return "redirect:/orders";
    }

    // ===== SCHIMBARE STARE =====
    @PostMapping("/orders/{id}/status")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam String newStatus) {
        orderService.updateStatus(id, newStatus);
        return "redirect:/orders/" + id;
    }

    // ===== ANULARE =====
    @PostMapping("/orders/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return "redirect:/orders";
    }
}
```

### 12. HomeController

```java
// controller/HomeController.java
@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String index() {
        return "index";
    }
}
```

### 13. Fișier de date inițiale (resources/data/init-data.txt)

```
# Format: clientName,clientEmail,description
Ion Popescu,ion.popescu@email.com,Laptop Dell XPS 15
Maria Ionescu,maria.ionescu@email.com,Mouse wireless Logitech
Andrei Dumitrescu,andrei.d@email.com,Tastatura mecanică
Elena Constantin,elena.c@email.com,Monitor 4K 27 inch
Mihai Georgescu,mihai.g@email.com,Webcam HD 1080p
```

### 14. Template-uri Thymeleaf

#### index.html (home page)
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Aplicatie</title></head>
<body>
<h1>Bun venit!</h1>
<nav>
    <a th:href="@{/orders}">Comenzile mele</a> |
    <a th:href="@{/orders/add}">Comandă nouă</a> |
    <a th:href="@{/admin/orders}">Admin</a>
</nav>
</body>
</html>
```

#### orders/index.html (lista comenzi)
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Comenzi</title></head>
<body>
<h1>Lista comenzi</h1>

<!-- Form filtrare -->
<form th:action="@{/orders}" method="get">
    <input name="email" type="text" placeholder="Email client"/>
    <button type="submit">Caută</button>
</form>

<a th:href="@{/orders/add}">+ Comandă nouă</a>

<table>
    <thead>
        <tr>
            <th>ID</th><th>Client</th><th>Dată</th><th>Stare</th><th>Acțiuni</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="order : ${orders}">
            <td th:text="${order.id}"></td>
            <td th:text="${order.clientName}"></td>
            <td th:text="${order.orderDate}"></td>
            <td th:text="${order.status}"></td>
            <td>
                <a th:href="@{'/orders/' + ${order.id}}">Detalii</a>
                <!-- Anulare doar dacă starea permite -->
                <form th:if="${order.status == 'PLASATA'}"
                      th:action="@{'/orders/' + ${order.id} + '/cancel'}"
                      method="post" style="display:inline">
                    <button type="submit">Anulează</button>
                </form>
            </td>
        </tr>
    </tbody>
</table>
</body>
</html>
```

#### orders/detail.html (detalii + schimbare stare admin)
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Detalii Comandă</title></head>
<body>
<h1>Comanda #<span th:text="${order.id}"></span></h1>

<p>Client: <span th:text="${order.clientName}"></span></p>
<p>Email: <span th:text="${order.clientEmail}"></span></p>
<p>Dată: <span th:text="${order.orderDate}"></span></p>
<p>Stare: <span th:text="${order.status}"></span></p>
<p>Descriere: <span th:text="${order.description}"></span></p>

<!-- Schimbare stare (admin) -->
<h3>Schimbă starea</h3>
<form th:action="@{'/orders/' + ${order.id} + '/status'}" method="post">
    <select name="newStatus">
        <option th:each="s : ${statuses}"
                th:value="${s}"
                th:text="${s}"></option>
    </select>
    <button type="submit">Actualizează starea</button>
</form>

<a th:href="@{/orders}">← Înapoi</a>
</body>
</html>
```

#### orders/edit.html (form add)
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Comandă nouă</title></head>
<body>
<h1>Plasează comandă</h1>
<form th:action="@{/orders/save}" method="post">
    <div>
        <label>Nume client:</label>
        <input name="clientName" type="text" required/>
    </div>
    <div>
        <label>Email:</label>
        <input name="clientEmail" type="email" required/>
    </div>
    <div>
        <label>Descriere comandă:</label>
        <textarea name="description" required></textarea>
    </div>
    <button type="submit">Plasează comanda</button>
</form>
<a th:href="@{/orders}">← Înapoi</a>
</body>
</html>
```

---

## Checklist final înainte de predare

- [ ] Aplicația pornește fără erori (`mvn spring-boot:run`)
- [ ] Accesibilă la `http://localhost:8080/home`
- [ ] Tabelele se populează automat din fișier text (minim 5 înregistrări)
- [ ] DTO-urile sunt folosite în loc de entități direct în view-uri
- [ ] Cel puțin un `th:each` pentru lista de entități
- [ ] Formulare funcționale (add, edit/status change)
- [ ] Tranzițiile de stare funcționează corect
- [ ] Notificările se salvează la fiecare schimbare de stare
- [ ] Filtrare/căutare funcțională pentru admin
- [ ] H2 Console accesibil (bonus: verifici că datele sunt în DB)
- [ ] Fără `@Autowired` pe câmpuri private (folosești constructor injection)
- [ ] Nicio entitate JPA folosită direct în template (doar DTO-uri)
