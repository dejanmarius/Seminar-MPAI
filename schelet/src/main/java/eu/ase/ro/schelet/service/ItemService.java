package eu.ase.ro.schelet.service;

import eu.ase.ro.schelet.dto.request.ItemRequest;
import eu.ase.ro.schelet.dto.response.ItemResponse;
import eu.ase.ro.schelet.model.Item;
import eu.ase.ro.schelet.model.ItemStatus;
import eu.ase.ro.schelet.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

/**
 * Logica de business: populare initiala din fisier, filtrare, creare, anulare
 * si schimbare de stare cu validarea tranzitiilor. Toate metodele publice
 * returneaza DTO-uri, niciodata entitatea {@link Item}.
 *
 * TODO: rename clasa -> OrderService / AppointmentService / RentalService
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final NotificationService notificationService;

    public ItemService(ItemRepository itemRepository,
                       NotificationService notificationService) {
        this.itemRepository = itemRepository;
        this.notificationService = notificationService;
    }

    // ===== INITIALIZARE DIN FISIER TEXT (ruleaza automat la pornire) =====
    @PostConstruct
    public void init() {
        InputStream is = getClass().getResourceAsStream("/data/init-data.txt");
        if (is == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",");
                // TODO: adapteaza la campurile entitatii tale
                // parts[0] = ownerName, parts[1] = ownerEmail, parts[2] = description
                Item item = new Item(parts[0].trim(), parts[1].trim(),
                                     parts[2].trim(), LocalDate.now());
                itemRepository.save(item);
            }
        } catch (Exception e) {
            System.err.println("Eroare la initializarea datelor: " + e.getMessage());
        }
    }

    // ===== READ =====

    // Filtrare combinata: stare + nume + data (toate optionale)
    public List<ItemResponse> filter(String status, String ownerName, String date) {
        ItemStatus itemStatus = (status != null && !status.isBlank())
                ? ItemStatus.valueOf(status) : null;
        String name = (ownerName != null && !ownerName.isBlank()) ? ownerName : null;
        LocalDate filterDate = (date != null && !date.isBlank())
                ? LocalDate.parse(date) : null;
        return itemRepository.findByFilters(itemStatus, name, filterDate).stream()
                .map(this::toResponse)
                .toList();
    }

    public ItemResponse findById(Long id) {
        return itemRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Inregistrarea nu a fost gasita: " + id));
    }

    // ===== CREATE =====

    public void create(ItemRequest request) {
        // TODO: adapteaza constructorul la campurile entitatii
        Item item = new Item(request.getOwnerName(), request.getOwnerEmail(),
                             request.getDescription(), LocalDate.now());
        itemRepository.save(item);
        notificationService.send(item.getOwnerEmail(),
                "Inregistrarea ta a fost creata cu succes!"); // TODO: mesaj specific domeniului
    }

    // ===== SCHIMBARE STARE =====

    public void updateStatus(Long id, String newStatusStr) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inregistrarea nu a fost gasita: " + id));

        ItemStatus newStatus = ItemStatus.valueOf(newStatusStr);

        // Validare: tranzitia trebuie sa fie permisa
        if (!item.getStatus().nextStates().contains(newStatus)) {
            throw new RuntimeException("Tranzitie nepermisa: "
                    + item.getStatus() + " -> " + newStatus);
        }

        item.setStatus(newStatus);
        itemRepository.save(item);
        notificationService.send(item.getOwnerEmail(),
                "Starea a fost actualizata: " + newStatus.name()); // TODO: mesaj specific
    }

    public void cancel(Long id) {
        updateStatus(id, "ANULATA"); // TODO: replace cu numele starii de anulare
    }

    // ===== MAPPER Entity -> DTO (nu expune entitatea JPA in view) =====

    private ItemResponse toResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getOwnerName(),
                item.getOwnerEmail(),
                item.getDescription(),
                item.getDate(),
                item.getStatus().name()  // .name() transforma enum in String
        );
    }
}
