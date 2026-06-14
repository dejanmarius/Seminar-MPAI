package eu.ase.ro.schelet.service;

import eu.ase.ro.schelet.dto.request.ResourceRequest;
import eu.ase.ro.schelet.dto.response.ResourceResponse;
import eu.ase.ro.schelet.model.Resource;
import eu.ase.ro.schelet.repository.ResourceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * CRUD complet pentru entitatea secundara gestionata de admin
 * (Doctor / Equipment / Product). Spre deosebire de Item, aici adminul
 * poate edita si sterge, nu doar schimba o stare.
 *
 * TODO: rename -> DoctorService / EquipmentService / ProductService
 */
@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    // Populeaza catalogul la pornire din /data/init-resources.txt
    @PostConstruct
    public void init() {
        InputStream is = getClass().getResourceAsStream("/data/init-resources.txt");
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
                Resource resource = new Resource(parts[0].trim(), parts[1].trim(),
                        Boolean.parseBoolean(parts[2].trim()));
                resourceRepository.save(resource);
            }
        } catch (Exception e) {
            System.err.println("Eroare la initializarea resurselor: " + e.getMessage());
        }
    }

    public List<ResourceResponse> getAll() {
        return resourceRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ResourceResponse> getAvailable() {
        return resourceRepository.findByAvailableTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public ResourceResponse findById(Long id) {
        return resourceRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Resursa nu a fost gasita: " + id));
    }

    public void create(ResourceRequest request) {
        Resource resource = new Resource(request.getName(),
                request.getDescription(), request.isAvailable());
        resourceRepository.save(resource);
    }

    public void update(Long id, ResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resursa nu a fost gasita: " + id));
        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setAvailable(request.isAvailable());
        resourceRepository.save(resource);
    }

    public void delete(Long id) {
        resourceRepository.deleteById(id);
    }

    private ResourceResponse toResponse(Resource resource) {
        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getDescription(),
                resource.isAvailable()
        );
    }
}
