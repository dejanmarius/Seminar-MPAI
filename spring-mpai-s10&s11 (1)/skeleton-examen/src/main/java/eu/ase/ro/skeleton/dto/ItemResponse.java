package eu.ase.ro.skeleton.dto;

import eu.ase.ro.skeleton.model.ItemState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private String description;
    private LocalDate date;
    private ItemState state;
    private SubItemResponse subItem;
}
