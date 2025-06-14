package com.takeitfree.itemmanagement.controllers;

import com.takeitfree.itemmanagement.dto.ItemPublicDTO;
import com.takeitfree.itemmanagement.dto.ItemRequestDTO;
import com.takeitfree.itemmanagement.dto.StatusDTO;
import com.takeitfree.itemmanagement.dto.StatusIdDTO;
import com.takeitfree.itemmanagement.services.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @PostMapping("/add")
    public ResponseEntity<?> addItem(
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
    public ResponseEntity<List<ItemRequestDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/by-title")
    public ResponseEntity<List<ItemRequestDTO>> getItemsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(itemService.getItemsByTitle(title));
    }

    @GetMapping("/by-location")
    public ResponseEntity<List<ItemRequestDTO>> getItemsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(itemService.getItemsByPostalCode(location));
    }

    @GetMapping("/by-city")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByCity(@RequestParam String city) {
        return ResponseEntity.ok(itemService.getItemsByCity(city));
    }

    @GetMapping("/by-taken")
    public ResponseEntity<List<ItemRequestDTO>> getItemsByTaken(@RequestParam boolean taken) {
        return ResponseEntity.ok(itemService.getItemsByTaken(taken));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateItem(@RequestBody @Valid ItemRequestDTO itemRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }
        return ResponseEntity.ok(itemService.updateItem(itemRequestDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.deleteItem(id));
    }

    @PutMapping("/take/{id}")
    public ResponseEntity<Boolean> markItemAsTaken(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.markItemAsTaken(id));
    }

    private List<String> formatValidationErrors(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
