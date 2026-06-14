package eu.ase.ro.examen.service;

import eu.ase.ro.examen.dto.request.OrderRequest;
import eu.ase.ro.examen.dto.response.OrderResponse;
import eu.ase.ro.examen.model.Order;
import eu.ase.ro.examen.model.OrderStatus;
import eu.ase.ro.examen.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

/**
 * Logica de business pentru comenzi: populare initiala din fisier, filtrare,
 * creare, anulare si schimbare de stare cu validarea tranzitiilor permise.
 * Toate metodele publice returneaza DTO-uri, niciodata entitatea {@link Order}.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    /** Populeaza baza de date la pornire cu comenzile din /data/init-data.txt. */
    @PostConstruct
    public void init() {
        InputStream is = getClass().getResourceAsStream("/data/init-data.txt");
        if (is == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",");
                Order order = new Order(parts[0].trim(), parts[1].trim(),
                        parts[2].trim(), LocalDate.now());
                orderRepository.save(order);
            }
        } catch (Exception e) {
            System.err.println("Eroare la initializarea datelor: " + e.getMessage());
        }
    }

    public List<OrderResponse> filter(String status, String clientName, String orderDate) {
        OrderStatus orderStatus = (status != null && !status.isBlank())
                ? OrderStatus.valueOf(status) : null;
        String name = (clientName != null && !clientName.isBlank()) ? clientName : null;
        LocalDate date = (orderDate != null && !orderDate.isBlank())
                ? LocalDate.parse(orderDate) : null;
        return orderRepository.findByFilters(orderStatus, name, date).stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse findById(Long id) {
        return orderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Comanda nu a fost gasita: " + id));
    }

    public void create(OrderRequest request) {
        Order order = new Order(request.getClientName(), request.getClientEmail(),
                request.getDescription(), LocalDate.now());
        orderRepository.save(order);
        notificationService.send(order.getClientEmail(),
                "Comanda ta a fost plasata cu succes!");
    }

    public void updateStatus(Long id, String newStatusStr) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comanda nu a fost gasita: " + id));

        OrderStatus newStatus = OrderStatus.valueOf(newStatusStr);

        if (!order.getStatus().nextStates().contains(newStatus)) {
            throw new RuntimeException("Tranzitie nepermisa: "
                    + order.getStatus() + " -> " + newStatus);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
        notificationService.send(order.getClientEmail(),
                "Starea comenzii tale a fost actualizata: " + newStatus.name());
    }

    public void cancel(Long id) {
        updateStatus(id, "ANULATA");
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getClientName(),
                order.getClientEmail(),
                order.getDescription(),
                order.getOrderDate(),
                order.getStatus().name()
        );
    }
}
