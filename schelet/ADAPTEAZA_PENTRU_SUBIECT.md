# Cum adaptezi scheletul la subiectul primit

## Structura finala a folderelor

```
src/main/java/eu/ase/ro/schelet/
├── ScheletApplication.java
├── controller/
│   ├── HomeController.java       ← nu se modifica (pagina cu 2 butoane)
│   ├── ItemController.java       ← CLIENT: lista + creare + anulare. rename + path-uri
│   └── AdminController.java      ← ADMIN: lista + filtru + schimbare stare. rename + path-uri
├── model/
│   ├── Item.java                 ← rename + campuri
│   ├── ItemStatus.java           ← rename + stari
│   └── Notification.java         ← nu se modifica
├── repository/
│   ├── ItemRepository.java       ← rename
│   └── NotificationRepository.java ← nu se modifica
├── service/
│   ├── ItemService.java          ← rename
│   └── NotificationService.java  ← nu se modifica
└── dto/
    ├── request/ItemRequest.java  ← rename + campuri
    └── response/ItemResponse.java ← rename + campuri

src/main/resources/templates/
├── index.html                    ← pagina cu butoane (client / admin / catalog)
├── items/                        ← CLIENT: index.html (lista, fara "Detalii"), edit.html (form)
└── admin/
    ├── items/                    ← ADMIN: index.html (lista + filtru), detail.html (schimbare stare)
    └── resources/                ← ADMIN: index.html (CRUD), edit.html (add/edit) — entitate secundara
```

## Entitatea principala (Item) vs. entitatea secundara (Resource)

Scheletul are DOUA grupuri de clase:

1. **Item** (= `Order` / `Appointment` / `RentalRequest`) — entitatea cu **stari**.
   Creata de utilizator, starea schimbata de admin. Are flux de stare + notificari.

2. **Resource** (= `Doctor` / `Equipment` / `Product`) — catalog cu **CRUD complet**
   gestionat DOAR de admin (creare, editare, **stergere**). NU are stari, dar are
   `available` (disponibilitate). Utilizatorul o ALEGE cand creeaza un Item.

| Subiect | Item (cu stari) | Resource (CRUD admin) |
|---------|-----------------|------------------------|
| S1 Magazin | Order | Product — **optional**, poti sterge tot grupul Resource |
| S2 Clinica | Appointment | Doctor — recomandat (pacientul alege medicul) |
| S3 Inchirieri | RentalRequest | **Equipment — OBLIGATORIU** (cerinta explicita) |

**Daca subiectul NU cere entitate secundara** (ex. S1 simplu): sterge clasele
`Resource*`, folderul `templates/admin/resources/`, `init-resources.txt` si
butonul "catalog" din `index.html`. Restul ramane neschimbat.

## Separarea client / admin (IMPORTANT)

- **Clientul** (`/items`) vede lista, creeaza si poate anula doar daca e in starea
  initiala. NU are link "Detalii" si NU schimba stari.
- **Adminul** (`/admin/items`) vede tot, filtreaza dupa **nume + stare + data**
  si schimba starea (cu validarea tranzitiilor din `nextStates()`).
- Schimbarea starii exista DOAR in `AdminController` + `admin/items/detail.html`.

---

## Subiect 1 - Magazin Virtual (Comenzi)

| Fisier | Schimbare |
|--------|-----------|
| `Item.java` | rename → `Order.java`, campuri: `clientName`, `clientEmail`, `description`, `orderDate` |
| `ItemStatus.java` | rename → `OrderStatus.java`, stari: `PLASATA, PROCESATA, EXPEDIATA, LIVRATA, ANULATA` |
| `ItemRepository.java` | rename → `OrderRepository.java` |
| `ItemService.java` | rename → `OrderService.java` |
| `ItemRequest.java` | rename → `OrderRequest.java`, campuri: `clientName`, `clientEmail`, `description` |
| `ItemResponse.java` | rename → `OrderResponse.java`, campuri: `clientName`, `clientEmail`, `description`, `orderDate` |
| `ItemController.java` | rename → `OrderController.java`, path-uri: `/orders` |
| `templates/items/` | rename folder → `orders/` |
| `init-data.txt` | format: `Ion Popescu,ion@email.com,Laptop Dell XPS` |

**nextStates():**
```java
case PLASATA   -> List.of(PROCESATA, ANULATA);
case PROCESATA -> List.of(EXPEDIATA);
case EXPEDIATA -> List.of(LIVRATA);
default        -> List.of();
```

---

## Subiect 2 - Clinica (Programari)

| Fisier | Schimbare |
|--------|-----------|
| `Item.java` | rename → `Appointment.java`, campuri: `patientName`, `patientEmail`, `doctorName`, `appointmentDate`, `reason` |
| `ItemStatus.java` | rename → `AppointmentStatus.java`, stari: `SOLICITATA, CONFIRMATA, EFECTUATA, ANULATA` |
| `ItemRepository.java` | rename → `AppointmentRepository.java` |
| `ItemService.java` | rename → `AppointmentService.java` |
| `ItemRequest.java` | rename → `AppointmentRequest.java`, campuri: `patientName`, `patientEmail`, `doctorName`, `reason` |
| `ItemResponse.java` | rename → `AppointmentResponse.java` |
| `ItemController.java` | rename → `AppointmentController.java`, path-uri: `/appointments` |
| `templates/items/` | rename folder → `appointments/` |
| `init-data.txt` | format: `Ion Popescu,ion@email.com,Dr. Ionescu,Consultatie` |
| `Resource.java` | rename → `Doctor.java`, campuri: `name`=nume medic, `description`=specializare |
| `Resource*` | rename grup → `Doctor*` (repository/service/controller/DTO) |
| `init-resources.txt` | format: `Dr. Ionescu,Cardiologie,true` |

**nextStates():**
```java
case SOLICITATA -> List.of(CONFIRMATA, ANULATA);
case CONFIRMATA -> List.of(EFECTUATA, ANULATA);
default         -> List.of();
```

---

## Subiect 3 - Inchirieri Echipamente

| Fisier | Schimbare |
|--------|-----------|
| `Item.java` | rename → `RentalRequest.java`, campuri: `userName`, `userEmail`, `equipmentName`, `startDate`, `endDate`, `purpose` |
| `ItemStatus.java` | rename → `RentalStatus.java`, stari: `CERUTA, APROBATA, PRELUATA, RETURNATA, RESPINSA` |
| `ItemRepository.java` | rename → `RentalRepository.java` |
| `ItemService.java` | rename → `RentalService.java` |
| `ItemRequest.java` | rename → `RentalRequest.java`, campuri: `userName`, `userEmail`, `equipmentName`, `purpose` |
| `ItemResponse.java` | rename → `RentalResponse.java` |
| `ItemController.java` | rename → `RentalController.java`, path-uri: `/rentals` |
| `templates/items/` | rename folder → `rentals/` |
| `init-data.txt` | format: `Ion Popescu,ion@email.com,Laborator chimie,Cercetare` |
| `Resource.java` | rename → `Equipment.java`, campuri: `name`=nume echipament, `available`=disponibil |
| `Resource*` | rename grup → `Equipment*` (OBLIGATORIU, CRUD complet de admin) |
| `init-resources.txt` | format: `Microscop optic,Laborator biologie,true` |

**nextStates():**
```java
case CERUTA  -> List.of(APROBATA, RESPINSA);
case APROBATA -> List.of(PRELUATA);
case PRELUATA -> List.of(RETURNATA);
default       -> List.of();
```

> **S3 - legatura Equipment ↔ RentalRequest:** la aprobare pune `equipment.available = false`,
> la returnare/respingere pune-l la loc `true`. Adauga in `Item` (RentalRequest) un camp
> `equipmentId` (sau `@ManyToOne Equipment`) ca sa stii ce echipament e cerut.

---

## Erori comune de evitat

1. **Numele campului in @Query trebuie sa fie exact ca in entitate**
   ```java
   // Daca campul se numeste "status" in entitate:
   @Query("... o.status = :status ...")  // corect
   @Query("... o.orderStatus = :status ...")  // GRESIT
   ```

2. **Constructor fara argumente OBLIGATORIU pe entitate**
   ```java
   public Order() {}  // nu sterge asta
   ```

3. **Status = STARE_INITIALA in constructorul de creare**
   ```java
   public Order(...) {
       ...
       this.status = OrderStatus.PLASATA;  // nu uita asta
   }
   ```

4. **application.yaml - indentare corecta**
   ```yaml
   spring:
     datasource:   # 2 spatii sub spring
       url: ...    # 4 spatii
     h2:           # 2 spatii sub spring
       console:
         enabled: true
   ```

5. **@Enumerated(EnumType.STRING) pe campul status**
   ```java
   @Enumerated(EnumType.STRING)  // salveaza "PLASATA" nu 0
   private OrderStatus status;
   ```
