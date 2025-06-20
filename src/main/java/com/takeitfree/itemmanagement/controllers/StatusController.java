package com.takeitfree.itemmanagement.controllers;

import com.takeitfree.itemmanagement.dto.ItemPublicDTO;
import com.takeitfree.itemmanagement.dto.StatusDTO;
import com.takeitfree.itemmanagement.services.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;

    // GET /status/items?name=Active
    @GetMapping("/items")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByStatusName(@RequestParam String name) {
        List<ItemPublicDTO> items = statusService.getItemsByStatusName(name);
        return ResponseEntity.ok(items);
    }

    // GET /status/items/id/1
    @GetMapping("/items/id/{id}")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByStatusId(@PathVariable Long id) {
        List<ItemPublicDTO> items = statusService.getItemsByStatusId(id);
        return ResponseEntity.ok(items);
    }

    // GET /status/items-all
    @GetMapping("/items-all")
    public ResponseEntity<List<ItemPublicDTO>> getAllStatusItemsNotTaken() {
        return ResponseEntity.ok(statusService.getAllStatusItems());
    }

    // GET /status/all
    @GetMapping("/all")
    public ResponseEntity<List<StatusDTO>> getAllStatus() {
        return ResponseEntity.ok(statusService.getAllStatus());
    }

    // GET /status/by-name?name=Active
    @GetMapping("/by-name")
    public ResponseEntity<?> getStatusByName(@RequestParam String name) {
        return ResponseEntity.ok(statusService.getStatusByName(name));
    }

    private List<String> formatValidationErrors(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
