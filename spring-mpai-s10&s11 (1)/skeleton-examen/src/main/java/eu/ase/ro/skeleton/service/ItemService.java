package eu.ase.ro.skeleton.service;

import eu.ase.ro.skeleton.dto.ItemRequest;
import eu.ase.ro.skeleton.dto.ItemResponse;
import eu.ase.ro.skeleton.mapper.ItemMapper;
import eu.ase.ro.skeleton.model.Item;
import eu.ase.ro.skeleton.model.ItemState;
import eu.ase.ro.skeleton.model.SubItem;
import eu.ase.ro.skeleton.repository.ItemRepository;
import eu.ase.ro.skeleton.repository.SubItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final SubItemRepository subItemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, SubItemRepository subItemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.subItemRepository = subItemRepository;
        this.itemMapper = itemMapper;
    }

    @PostConstruct
    private void init() {
        // Cerinta: La pornirea aplicatiei tabelele sunt populate automat cu cel putin 5 inregistrari. Datele sunt preluate dintr-un fisier text.
        // Aici respectam exact modul in care profesorul folosea @PostConstruct
        try {
            InputStream is = getClass().getResourceAsStream("/data.txt");
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens.length == 5) {
                        String subItemName = tokens[0];
                        String subItemDetails = tokens[1];
                        String itemDesc = tokens[2];
                        LocalDate date = LocalDate.parse(tokens[3]);
                        ItemState state = ItemState.valueOf(tokens[4]);

                        // Cautam sau cream SubItem
                        SubItem subItem = new SubItem(subItemName, subItemDetails);
                        subItem = subItemRepository.save(subItem);

                        // Cream Item
                        Item item = new Item(itemDesc, date, state, subItem);
                        itemRepository.save(item);
                    }
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ItemResponse> getAll() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    public ItemResponse findById(Long id) {
        return itemRepository.findById(id)
                .map(itemMapper::toItemResponse)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void newItem(ItemRequest request) {
        SubItem subItem = subItemRepository.findById(request.getSubItemId())
                .orElseThrow(() -> new RuntimeException("SubItem not found"));

        Item item = new Item(request.getDescription(), request.getDate(), request.getState(), subItem);
        itemRepository.save(item);
    }

    public void updateById(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        SubItem subItem = subItemRepository.findById(request.getSubItemId())
                .orElseThrow(() -> new RuntimeException("SubItem not found"));

        item.setDescription(request.getDescription());
        item.setDate(request.getDate());
        item.setState(request.getState());
        item.setSubItem(subItem);

        itemRepository.save(item);
    }
    
    public List<SubItem> getAllSubItems() {
        return subItemRepository.findAll();
    }
}
