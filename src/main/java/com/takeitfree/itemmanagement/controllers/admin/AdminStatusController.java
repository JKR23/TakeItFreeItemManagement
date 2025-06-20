package com.takeitfree.itemmanagement.controllers.admin;


import com.takeitfree.itemmanagement.dto.StatusDTO;
import com.takeitfree.itemmanagement.services.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/status")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminStatusController {
    private final StatusService statusService;


    // POST /status/add
    @PostMapping("/add")
    public ResponseEntity<?> addStatus(@RequestBody @Valid StatusDTO status, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }
        return ResponseEntity.ok(statusService.addStatus(status));
    }


    // DELETE /status/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStatus(@PathVariable Long id) {
        String result = statusService.deleteStatus(id);
        return ResponseEntity.ok(result);
    }

    // PUT /status/update
    @PutMapping("/update")
    public ResponseEntity<?> updateStatus(@RequestBody @Valid StatusDTO status, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }
        return ResponseEntity.ok(statusService.updateStatus(status));
    }

    private List<String> formatValidationErrors(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
