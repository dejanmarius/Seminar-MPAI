package eu.ase.ro.skeleton.mapper;

import eu.ase.ro.skeleton.dto.ItemResponse;
import eu.ase.ro.skeleton.dto.SubItemResponse;
import eu.ase.ro.skeleton.model.Item;
import eu.ase.ro.skeleton.model.SubItem;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemResponse toItemResponse(Item item) {
        if (item == null) {
            return null;
        }

        SubItemResponse subItemResponse = null;
        if (item.getSubItem() != null) {
            subItemResponse = new SubItemResponse(
                    item.getSubItem().getId(),
                    item.getSubItem().getName(),
                    item.getSubItem().getDetails()
            );
        }

        return new ItemResponse(
                item.getId(),
                item.getDescription(),
                item.getDate(),
                item.getState(),
                subItemResponse
        );
    }
}
