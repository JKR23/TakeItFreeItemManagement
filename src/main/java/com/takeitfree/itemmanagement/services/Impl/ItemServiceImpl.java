package com.takeitfree.itemmanagement.services.Impl;

import com.takeitfree.itemmanagement.dto.*;
import com.takeitfree.itemmanagement.models.GeocodingResult;
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

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ObjectValidator objectValidator;
    private final GeocodingService geocodingService;

    @Override
    public String addItem(ItemRequestDTO itemRequestDTO) {

        objectValidator.validate(itemRequestDTO);

        if (itemRequestDTO.getPostalCode()!=null && !itemRequestDTO.getPostalCode().isEmpty()) {

            GeocodingResult geocodingResult = geocodingService.geocodePostalCode(itemRequestDTO.getPostalCode());

            itemRequestDTO.setLatitude(geocodingResult.getLatitude());
            itemRequestDTO.setLongitude(geocodingResult.getLongitude());
            itemRequestDTO.setCity(geocodingResult.getCity());

        }

        Item newItem = ItemRequestDTO.toEntity(itemRequestDTO);

        newItem.setTaken(false);

        itemRepository.save(newItem);

        return "Item added successfully";
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
    public List<ItemRequestDTO> getItemsByLocalization(String localization) {
        objectValidator.validate(localization);
        return ItemRequestDTO.toDTO(itemRepository.findByLocalization(localization));
    }

    @Override
    public List<ItemRequestDTO> getItemsByDistance(Float distance) {
        objectValidator.validate(distance);
        return ItemRequestDTO.toDTO(itemRepository.findByDistance(distance));
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

            Item item = itemOptional.get();

            // Si un code postal is provided, we update
            if (itemRequestDTO.getPostalCode() != null && !itemRequestDTO.getPostalCode().isEmpty()) {
                GeocodingResult geocodingResult = geocodingService.geocodePostalCode(itemRequestDTO.getPostalCode());
                itemRequestDTO.setLatitude(geocodingResult.getLatitude());
                itemRequestDTO.setLongitude(geocodingResult.getLongitude());
                itemRequestDTO.setCity(geocodingResult.getCity());
            }

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
}
