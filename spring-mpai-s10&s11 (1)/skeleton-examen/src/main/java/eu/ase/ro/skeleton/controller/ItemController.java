package eu.ase.ro.skeleton.controller;

import eu.ase.ro.skeleton.dto.ItemRequest;
import eu.ase.ro.skeleton.dto.ItemResponse;
import eu.ase.ro.skeleton.model.ItemState;
import eu.ase.ro.skeleton.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String navigateToItemsPage(Model model) {
        List<ItemResponse> items = itemService.getAll();
        model.addAttribute("items", items);
        return "items/index";
    }

    @GetMapping("/items/{id}/edit")
    public String navigateToEditItemPage(@PathVariable Long id, Model model) {
        ItemResponse response = itemService.findById(id);
        
        // Cream un ItemRequest pe baza lui ItemResponse pentru formular
        ItemRequest request = new ItemRequest();
        request.setDescription(response.getDescription());
        request.setDate(response.getDate());
        request.setState(response.getState());
        if (response.getSubItem() != null) {
            request.setSubItemId(response.getSubItem().getId());
        }

        model.addAttribute("item", request);
        model.addAttribute("itemId", id);
        model.addAttribute("states", ItemState.values());
        model.addAttribute("subItems", itemService.getAllSubItems());

        return "items/edit";
    }

    @GetMapping("/items/add")
    public String navigateToAddItemPage(Model model) {
        model.addAttribute("item", new ItemRequest());
        model.addAttribute("itemId", null); // explicit null pentru noul item
        model.addAttribute("states", ItemState.values());
        model.addAttribute("subItems", itemService.getAllSubItems());
        return "items/edit";
    }

    // Aici folosim aceeasi abordare ca profesorul: un singur endpoint de save, care distinge add/edit pe baza ID-ului
    @PostMapping("/items/save")
    public String save(@ModelAttribute ItemRequest request,
                       @RequestParam(required = false) Long itemId) {
        
        if(itemId == null){
            itemService.newItem(request);
        } else {
            itemService.updateById(itemId, request);
        }
        return "redirect:/items";
    }
}
