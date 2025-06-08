package com.takeitfree.itemmanagement.controllers;

import com.takeitfree.itemmanagement.dto.CategoryDTO;
import com.takeitfree.itemmanagement.dto.ItemPublicDTO;
import com.takeitfree.itemmanagement.dto.ItemRequestDTO;
import com.takeitfree.itemmanagement.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // GET /category/all
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    // GET /category/by-name?name=X
    @GetMapping("/by-name")
    public ResponseEntity<CategoryDTO> getCategoryByName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    // POST /category/add
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }

        return ResponseEntity.ok(categoryService.addCategory(categoryDTO));
    }

    // PUT /category/update
    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatValidationErrors(result));
        }

        return ResponseEntity.ok(categoryService.updateCategory(categoryDTO));
    }

    // DELETE /category/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    // GET /category/items?name=X
    @GetMapping("/items")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByCategoryName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.getItemsByCategoryName(name));
    }

    // GET /category/items/id/1
    @GetMapping("/items/id/{id}")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByCategoryName(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getItemsByCategoryId(id));
    }

    // GET /category/items/id/1
    @GetMapping("/items-all")
    public ResponseEntity<List<ItemPublicDTO>> getItemsByCategoryName() {
        return ResponseEntity.ok(categoryService.getAllCategoryItems());
    }

    private List<String> formatValidationErrors(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
