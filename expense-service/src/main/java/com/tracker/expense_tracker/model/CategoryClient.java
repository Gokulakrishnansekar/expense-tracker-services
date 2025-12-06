package com.tracker.expense_tracker.model;


import com.tracker.shared_services.DTO.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "category-service")
public interface CategoryClient {
    @GetMapping("/api/category/{id}")
    public CategoryDto getCategoryById(@PathVariable() Long id);
    @GetMapping("/api/categoryByName")
    public List<CategoryDto> getCategoryByName(@RequestParam() String name);
    }

@Component
class CategoryClientFallback implements CategoryClient {
    @Override
    public CategoryDto getCategoryById(Long id) {
        System.out.println("⚠️ Category service unreachable, returning fallback data");
        return new CategoryDto(null,null,null);
    }
    public List<CategoryDto> getCategoryByName(@RequestParam() String name){
        System.out.println("⚠️ Category service unavailable, returning fallback data");
         return List.of(new CategoryDto(null, null,null));
    }
}
