package com.takeitfree.itemmanagement.controllers;

import com.takeitfree.itemmanagement.dto.ItemDTO;
import com.takeitfree.itemmanagement.dto.StatusDTO;
import com.takeitfree.itemmanagement.services.StatusService;
import jakarta.validation.Valid;
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

    // POST /status/add
    @PostMapping("/add")
    public ResponseEntity<?> addStatus(@RequestBody @Valid StatusDTO status, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }
        return ResponseEntity.ok(statusService.addStatus(status));
    }

    // PUT /status/update
    @PutMapping("/update")
    public ResponseEntity<?> updateStatus(@RequestBody @Valid StatusDTO status, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }
        return ResponseEntity.ok(statusService.updateStatus(status));
    }

    // DELETE /status/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStatus(@PathVariable Long id) {
        String result = statusService.deleteStatus(id);
        return ResponseEntity.ok(result);
    }

    // GET /status/items?name=Active
    @GetMapping("/items")
    public ResponseEntity<List<ItemDTO>> getItemsByStatusName(@RequestParam String name) {
        List<ItemDTO> items = statusService.getItemsByStatusName(name);
        return ResponseEntity.ok(items);
    }

    private List<String> formatValidationErrors(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
