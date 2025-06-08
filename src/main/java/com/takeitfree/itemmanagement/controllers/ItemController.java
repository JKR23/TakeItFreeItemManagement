package com.takeitfree.itemmanagement.controllers;

import com.takeitfree.itemmanagement.dto.ItemRequestDTO;
import com.takeitfree.itemmanagement.services.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestBody @Valid ItemRequestDTO itemRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }
        return ResponseEntity.ok(itemService.addItem(itemRequestDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/by-title")
    public ResponseEntity<List<ItemRequestDTO>> getItemsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(itemService.getItemsByTitle(title));
    }

    @GetMapping("/by-localization")
    public ResponseEntity<List<ItemRequestDTO>> getItemsByLocalization(@RequestParam String localization) {
        return ResponseEntity.ok(itemService.getItemsByLocalization(localization));
    }

    @GetMapping("/by-distance")
    public ResponseEntity<List<ItemRequestDTO>> getItemsByDistance(@RequestParam Float distance) {
        return ResponseEntity.ok(itemService.getItemsByDistance(distance));
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
