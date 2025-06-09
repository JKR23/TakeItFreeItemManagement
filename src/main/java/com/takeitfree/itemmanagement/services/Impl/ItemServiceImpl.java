package com.takeitfree.itemmanagement.services.Impl;

import com.takeitfree.itemmanagement.dto.*;
import com.takeitfree.itemmanagement.dto.GeoLocationIQResponse;
import com.takeitfree.itemmanagement.models.Item;
import com.takeitfree.itemmanagement.repositories.ItemRepository;
import com.takeitfree.itemmanagement.services.GeocodingService;
import com.takeitfree.itemmanagement.services.ItemService;
import com.takeitfree.itemmanagement.validators.ObjectValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ObjectValidator objectValidator;
    private final GeocodingService geocodingService;

    @Override
    public String addItem(ItemRequestDTO itemRequestDTO) {

        try {
            objectValidator.validate(itemRequestDTO);

            if (itemRequestDTO.getPostalCode() == null || itemRequestDTO.getPostalCode().isEmpty()) {
                throw new RuntimeException("Postal code is required");
            }

            LocationData locationData = geocodingService.getLocationFromPostalCode(itemRequestDTO.getPostalCode());

            itemRequestDTO.setLatitude(locationData.getLatitude());
            itemRequestDTO.setLongitude(locationData.getLongitude());
            itemRequestDTO.setCity(locationData.getCity());

            Item newItem = ItemRequestDTO.toEntity(itemRequestDTO);

            newItem.setTaken(false);

            itemRepository.save(newItem);

            return "Item added successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ItemRequestDTO> getAllItems() {
        return ItemRequestDTO.toDTO(itemRepository.findAll());
    }

    @Override
    public List<ItemRequestDTO> getItemsByTitle(String title) {
        objectValidator.validate(title);
        return ItemRequestDTO.toDTO(itemRepository.findByTitleContainingIgnoreCase(title));
    }

    @Override
    public List<ItemRequestDTO> getItemsByPostalCode(String postalCode) {
        objectValidator.validate(postalCode);
        return ItemRequestDTO.toDTO(itemRepository.findByPostalCode(postalCode));
    }

    @Override
    public List<ItemRequestDTO> getItemsByTaken(boolean taken) {
        objectValidator.validate(taken);
        return ItemRequestDTO.toDTO(itemRepository.findByTaken(taken));
    }

    @Override
    public String updateItem(ItemRequestDTO itemRequestDTO) {
        try {
            objectValidator.validate(itemRequestDTO);

            Optional<Item> itemOptional = itemRepository.findById(itemRequestDTO.getId());

            if (itemOptional.isEmpty()) {
                throw new EntityNotFoundException("Item not found");
            }

            // Si un code postal is provided, we update
            if (itemRequestDTO.getPostalCode() == null || itemRequestDTO.getPostalCode().isEmpty()) {
                throw new RuntimeException("Postal code is missing");
            }

            LocationData locationData = geocodingService.getLocationFromPostalCode(itemRequestDTO.getPostalCode());
            itemRequestDTO.setLatitude(locationData.getLatitude());
            itemRequestDTO.setLongitude(locationData.getLongitude());
            itemRequestDTO.setCity(locationData.getCity());

            Item item = itemOptional.get();

            // Mise à jour des champs
            item.setTitle(itemRequestDTO.getTitle());
            item.setImage(itemRequestDTO.getImage());
            item.setStatus(StatusDTO.toEntity(StatusIdDTO.toDTO(itemRequestDTO.getStatusId())));
            item.setLatitude(itemRequestDTO.getLatitude());
            item.setLongitude(itemRequestDTO.getLongitude());
            item.setCity(itemRequestDTO.getCity());

            itemRepository.save(item);

            return "Item updated successfully";
        } catch (EntityExistsException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String deleteItem(Long id) {
        try {
            objectValidator.validate(id);

            Optional<Item> item = itemRepository.findById(id);

            if (item.isEmpty()) {
                throw new EntityNotFoundException("Item not found");
            }

            itemRepository.delete(item.get());

            return "Item deleted successfully";
        } catch (EntityExistsException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean markItemAsTaken(Long id) {
        try {
            objectValidator.validate(id);

            Optional<Item> item = itemRepository.findById(id);

            if (item.isEmpty()) {
                throw new EntityNotFoundException("Item not found");
            }

            boolean availability = item.get().isTaken();

            item.get().setTaken(!availability);

            itemRepository.save(item.get());

            return item.get().isTaken();

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ItemPublicDTO> getItemsByCity(String city) {
        try{
            objectValidator.validate(city);

            List<Item> itemList = itemRepository.findByCityContainingIgnoreCase(city);

            if (itemList.isEmpty()) {
                throw new EntityNotFoundException("Item not found");
            }

            return itemList.stream()
                    .filter(item -> !item.isTaken())
                    .map(ItemPublicDTO::toDTO)
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
