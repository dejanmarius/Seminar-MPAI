package eu.ase.ro.examen.dto.response;

import java.time.LocalDate;

public class OrderResponse {

    private Long id;
    private String clientName;
    private String clientEmail;
    private String description;
    private LocalDate orderDate;
    private String status;

    public OrderResponse() {}

    public OrderResponse(Long id, String clientName, String clientEmail,
                         String description, LocalDate orderDate, String status) {
        this.id = id;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.description = description;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
