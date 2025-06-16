package com.takeitfree.itemmanagement.services.Impl;

import com.takeitfree.itemmanagement.dto.*;
import com.takeitfree.itemmanagement.exceptions.ImageProcessingException;
import com.takeitfree.itemmanagement.exceptions.ItemProcessingException;
import com.takeitfree.itemmanagement.models.Item;
import com.takeitfree.itemmanagement.repositories.ItemRepository;
import com.takeitfree.itemmanagement.services.AzureBlobStorageService;
import com.takeitfree.itemmanagement.services.GeocodingService;
import com.takeitfree.itemmanagement.services.ItemService;
import com.takeitfree.itemmanagement.validators.ObjectValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ObjectValidator objectValidator;
    private final GeocodingService geocodingService;
    private final AzureBlobStorageService azureBlobStorageService;

    @Override
    @Transactional
    public String addItem(ItemRequestDTO itemRequestDTO) {

        try {

            // Process validation
            objectValidator.validate(itemRequestDTO);
            validateRequiredFields(itemRequestDTO);

            //process location
            LocationData locationData = geocodingService.getLocationFromPostalCode(itemRequestDTO.getPostalCode());
            updateItemWithLocation(itemRequestDTO, locationData);

            // Extract url of image from Azure
            String imageUrl = processItemImage(itemRequestDTO.getUrlImage());

            // process new entity with urlImage
            Item newItem = createItemEntity(itemRequestDTO, imageUrl);

            itemRepository.save(newItem);

            return "Item added successfully";

        } catch (Exception e) {
            throw new ItemProcessingException("Failed to add item: " + e.getMessage(), e);
        }

    }

    @Override
    public List<ItemPublicDTO> getAllItems() {
        return ItemPublicDTO.toDTO(itemRepository.findAll());
    }

    @Override
    public List<ItemPublicDTO> getItemsByTitle(String title) {
        objectValidator.validate(title);
        return ItemPublicDTO.toDTO(itemRepository.findByTitleContainingIgnoreCase(title));
    }

    @Override
    public List<ItemPublicDTO> getItemsByPostalCode(String postalCode) {
        objectValidator.validate(postalCode);
        return ItemPublicDTO.toDTO(itemRepository.findByPostalCode(postalCode));
    }

    @Override
    public List<ItemPublicDTO> getItemsByTaken(boolean taken) {
        objectValidator.validate(taken);
        return ItemPublicDTO.toDTO(itemRepository.findByTaken(taken));
    }

    @Override
    @Transactional
    public String updateItem(ItemRequestDTO itemRequestDTO) {
        try {
            objectValidator.validate(itemRequestDTO);
            validateRequiredFields(itemRequestDTO);

            Optional<Item> itemOptional = itemRepository.findById(itemRequestDTO.getId());

            if (itemOptional.isEmpty()) {
                throw new EntityNotFoundException("Item not found");
            }

            // Process update location
            LocationData locationData = geocodingService.getLocationFromPostalCode(itemRequestDTO.getPostalCode());
            updateItemWithLocation(itemRequestDTO, locationData);

            // Process UrlImage
            String urlImage = processItemImage(itemRequestDTO.getUrlImage());
            Item updatedItem = createItemEntity(itemRequestDTO, urlImage);

            // Process title and status
            updateTitleAndStatus(updatedItem, itemRequestDTO.getTitle(), itemRequestDTO.getStatusId());

            //save_update item
            itemRepository.save(updatedItem);

            return "Item updated successfully";
        } catch (EntityExistsException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
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

    private void validateRequiredFields(ItemRequestDTO itemRequestDTO) {
        // Validate postal code
        if (itemRequestDTO.getPostalCode() == null || StringUtils.isEmpty(itemRequestDTO.getPostalCode())) {
            throw new IllegalArgumentException("Postal code is required");
        }

        // Validate image(extension, size, empty, null)
        validateImageFile(itemRequestDTO.getUrlImage());
    }

    private void validateImageFile(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        // Optional additional validations
        if (!List.of("image/jpeg", "image/png", "image/jpg", "image/webp").contains(imageFile.getContentType())) {
            throw new IllegalArgumentException("Only JPEG/PNG/WEBP images are allowed");
        }

        if (imageFile.getSize() > 5_000_000) { // 5MB limit
            throw new IllegalArgumentException("Image size must be less than 5MB");
        }
    }

    private void updateItemWithLocation(ItemRequestDTO itemRequestDTO, LocationData locationData) {
        itemRequestDTO.setLatitude(locationData.getLatitude());
        itemRequestDTO.setLongitude(locationData.getLongitude());
        itemRequestDTO.setCity(locationData.getCity());
    }

    private String processItemImage(MultipartFile imageUrl) throws IOException {
        try {
            return azureBlobStorageService.uploadFile(imageUrl);
        } catch (IOException e) {
            //log.error("Failed to upload image to Cloudinary", e);
            throw new ImageProcessingException("Failed to process image", e);
        }
    }

    private Item createItemEntity(ItemRequestDTO itemRequestDTO, String imageUrl) {
        Item item = ItemRequestDTO.toEntity(itemRequestDTO);
        item.setImage(imageUrl);
        item.setTaken(false);
        return item;
    }

    private void updateTitleAndStatus(Item item, String title, StatusIdDTO statusIdDTO) {

        item.setTitle(title);
        item.setStatus(StatusDTO.toEntity(StatusIdDTO.toDTO(statusIdDTO)));

    }
}
