package com.service.category_service.service;

import com.service.category_service.dto.CategoryDto;
import com.service.category_service.entity.CategoryEntity;
import com.service.category_service.repository.CategoryRepo;
import com.tracker.shared_services.kafka.constants.Status;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    public static CategoryEntity Category;

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepo categoryRepo;

    @BeforeAll
    public static  void initial(){
        new CategoryEntity();
        Category= CategoryEntity.builder().id(1L).description("test description").lastModifiedDate(null).name("test").status(Status.Active).build();
    }

    @Test
    void getAllCategory() {
        Mockito.when(categoryRepo.findByStatus(Status.Active)).thenReturn(List.of(Category));

        List<CategoryDto> list=   categoryService.getAllCategory();
        assertNotNull(list);
        assertEquals(1,list.size());
        assertEquals("test",list.get(0).name());


    }

    @Test
    void getCategoryById() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.of(Category));
        CategoryDto categoryDto=  categoryService.getCategoryById(1L);
        assertEquals(1L,categoryDto.id());
    }

    @Test
    void getCategoryByIdWhenNull() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.empty());
        //CategoryDto categoryDto=  categoryService.getCategoryById(1L);
       assertThrows(RuntimeException.class,()->categoryService.getCategoryById(1L));
    }

    @Test
    void createCategory() {
    }

    @Test
    void updateCategory() {
    }

    @Test
    void findByName() {
    }

    @Test
    void deleteCategory() {
    }
}