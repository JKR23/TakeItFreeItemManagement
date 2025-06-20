package com.takeitfree.itemmanagement.controllers;

import com.takeitfree.itemmanagement.dto.ItemPublicDTO;
import com.takeitfree.itemmanagement.dto.ItemRequestDTO;
import com.takeitfree.itemmanagement.dto.StatusIdDTO;
import com.takeitfree.itemmanagement.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/publisher-items")
    public ResponseEntity<?> publishItem(
            @RequestParam("title") String title,
            @RequestParam("postalCode") String postalCode,
            @RequestParam("statusId") Long statusId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        try {

            System.out.println("Image reçue ? " + (imageFile != null ? imageFile.getOriginalFilename() : "Aucune"));

            // Création of StatusIdDTO from a simple id
            StatusIdDTO statusDTO = StatusIdDTO.builder().id(statusId).build();

            // Création of main DTO
            ItemRequestDTO dto = ItemRequestDTO.builder()
                    .title(title)
                    .postalCode(postalCode)
                    .statusId(statusDTO)
                    .urlImage(imageFile)
                    .build();

            return ResponseEntity.ok(itemService.addItem(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during creation of item: " + e.getMessage()+" "+e.getCause());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemPublicDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/my-items")
    public ResponseEntity<List<ItemPublicDTO>> getMyAllItems() {
        return ResponseEntity.ok(itemService.getMyAllItems());
    }

    @GetMapping("/by-title")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(itemService.getItemsByTitle(title));
    }

    @GetMapping("/by-location")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(itemService.getItemsByPostalCode(location));
    }

    @GetMapping("/by-city")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByCity(@RequestParam String city) {
        return ResponseEntity.ok(itemService.getItemsByCity(city));
    }

    @GetMapping("/by-taken")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByTaken(@RequestParam boolean taken) {
        return ResponseEntity.ok(itemService.getItemsByTaken(taken));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateItem(@RequestParam Long id,
                                        @RequestParam("title") String title,
                                        @RequestParam("postalCode") String postalCode,
                                        @RequestParam("statusId") Long statusId,
                                        @RequestParam("image") MultipartFile imageFile) {

        try {
            StatusIdDTO statusDTO = StatusIdDTO.builder().id(statusId).build();

            ItemRequestDTO itemRequestDTO = ItemRequestDTO.builder()
                    .id(id)
                    .title(title)
                    .postalCode(postalCode)
                    .statusId(statusDTO)
                    .urlImage(imageFile)
                    .build();

            return ResponseEntity.ok(itemService.updateItem(itemRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during updating item: " + e.getMessage()+" "+e.getCause());
        }
    }


    @PutMapping("/marked-taken/{id}")
    public ResponseEntity<Boolean> markItemAsTaken(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.markItemAsTaken(id));
    }
}
