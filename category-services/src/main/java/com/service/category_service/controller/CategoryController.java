package com.service.category_service.controller;

import com.service.category_service.dto.CategoryDto;
import com.service.category_service.dto.CategoryReqDto;
import com.service.category_service.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;


    public String  port;
    CategoryController(CategoryService categoryService){
        this.categoryService=categoryService;
    }
    @Operation(description = "Get Category")
    @GetMapping("/")
     public List<CategoryDto> getAllCategory(){
      return   categoryService.getAllCategory();
    }

    @Operation(description = "Get Category by Id")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable() Long id){
        return   categoryService.getCategoryById(id);
    }

    @Operation(description = "create  Category")
    @PostMapping("/")
    public CategoryDto createCategory(@RequestBody() CategoryReqDto categoryDto){
        return  categoryService.createCategory(categoryDto);
    }

    @Operation(description = "update  Category")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,@RequestParam() CategoryDto categoryDto){

        return  categoryService.updateCategory(id,categoryDto);
    }
    @Operation(description = "Get Category by name")
    @GetMapping("/categoryByName")
    public List<CategoryDto> getCategoryByName(@RequestParam() String name){
        return   categoryService.findByName(name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
      return  categoryService.deleteCategory(id);

    }

}
