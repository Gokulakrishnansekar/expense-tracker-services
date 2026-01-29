package com.service.category_service.controller;

import com.service.category_service.dto.CategoryDto;
import com.service.category_service.entity.CategoryEntity;
import com.service.category_service.service.CategoryService;
import com.tracker.shared_services.kafka.constants.Status;
import org.aspectj.lang.annotation.Before;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    MockMvcTester mvc;
    @MockitoBean
    private CategoryService categoryService;

    static CategoryEntity category;

    @BeforeAll
    public static void init(){
        category= new CategoryEntity().builder().id(1L).name("test").status(Status.Active).description("dummy").lastModifiedDate( LocalDate.now()).build();


    }


    @Test
    void getAllCategory() {
        CategoryDto categoryDto=new CategoryDto(category.getId(),category.getName(),category.getDescription());
        Mockito.when(categoryService.getAllCategory()).thenReturn(List.of(categoryDto));
        mvc.get().uri("/api/category/").assertThat().hasStatusOk().hasContentType(MediaType.APPLICATION_JSON).
                bodyJson().extractingPath("$[0].id").isEqualTo(1);
    }

    @Test
    void getCategoryById() {
        CategoryDto categoryDto=new CategoryDto(category.getId(),category.getName(),category.getDescription());
        Mockito.when(categoryService.getCategoryById(2L)).thenReturn(categoryDto);
        mvc.get().uri("/api/category/2").assertThat().hasStatusOk().hasContentType(MediaType.APPLICATION_JSON).
                bodyJson().extractingPath("$.id").isEqualTo(1);
    }
    @Test
    void getCategoryByIdWithException() {

        Mockito.when(categoryService.getCategoryById(2L)).thenThrow(new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "category is not present"
        ));
        mvc.get().uri("/api/category/2").assertThat().hasStatus4xxClientError();
    }


    @Test
    void createCategory() {
    }

    @Test
    void updateCategory() {
    }

    @Test
    void getCategoryByName() {
    }

    @Test
    void deleteCategory() {
    }
}