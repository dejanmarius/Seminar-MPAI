package eu.ase.ro.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reprezinta Comanda / CerereInchiriere / Programare
    private String description;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private ItemState state;

    @ManyToOne
    @JoinColumn(name = "sub_item_id")
    private SubItem subItem; // Relatie cu Medic/Echipament/Client

    public Item() {
    }

    public Item(String description, LocalDate date, ItemState state, SubItem subItem) {
        this.description = description;
        this.date = date;
        this.state = state;
        this.subItem = subItem;
    }
}
