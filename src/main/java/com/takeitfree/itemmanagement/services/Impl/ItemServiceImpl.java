package com.takeitfree.itemmanagement.services.Impl;

import com.takeitfree.itemmanagement.dto.*;
import com.takeitfree.itemmanagement.models.Item;
import com.takeitfree.itemmanagement.repositories.ItemRepository;
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

    @Override
    public String addItem(ItemDTO itemDTO) {

        objectValidator.validate(itemDTO);

        itemRepository.save(ItemDTO.toEntity(itemDTO));

        return "Item added successfully";
    }

    @Override
    public List<ItemDTO> getAllItems() {
        return ItemDTO.toDTO(itemRepository.findAll());
    }

    @Override
    public List<ItemDTO> getItemsByTitle(String title) {
        objectValidator.validate(title);
        return ItemDTO.toDTO(itemRepository.findByTitleContainingIgnoreCase(title));
    }

    @Override
    public List<ItemDTO> getItemsByLocalization(String localization) {
        objectValidator.validate(localization);
        return ItemDTO.toDTO(itemRepository.findByLocalization(localization));
    }

    @Override
    public List<ItemDTO> getItemsByDistance(Float distance) {
        objectValidator.validate(distance);
        return ItemDTO.toDTO(itemRepository.findByDistance(distance));
    }

    @Override
    public List<ItemDTO> getItemsByTaken(boolean taken) {
        objectValidator.validate(taken);
        return ItemDTO.toDTO(itemRepository.findByTaken(taken));
    }

    @Override
    public String updateItem(ItemDTO itemDTO) {
        try {
            objectValidator.validate(itemDTO);

            Optional<Item> item = itemRepository.findById(itemDTO.getId());

            if (item.isEmpty()) {
                throw new EntityNotFoundException("Item not found");
            }

            item.get().setTitle(itemDTO.getTitle());
            item.get().setImage(itemDTO.getImage());
            item.get().setCategory(CategoryDTO.toEntity(
                    CategoryIdDTO.toDTO(itemDTO.getCategoryId()))
            );
            item.get().setStatus(StatusDTO.toEntity(
                    StatusIdDTO.toDTO(itemDTO.getStatusId()))
            );
            item.get().setDescription(itemDTO.getDescription());
            item.get().setLocalization(itemDTO.getLocalization());
            item.get().setDistance(itemDTO.getDistance());
            item.get().setTaken(itemDTO.isTaken());

            itemRepository.save(item.get());

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
