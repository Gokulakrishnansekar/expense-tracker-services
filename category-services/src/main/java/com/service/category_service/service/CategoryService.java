package com.service.category_service.service;

import com.service.category_service.dto.CategoryDto;
import com.service.category_service.dto.CategoryReqDto;
import com.service.category_service.entity.CategoryEntity;
import com.service.category_service.kafka_producer.CategoryEventProducer;
import com.service.category_service.repository.CategoryRepo;
import com.sun.jdi.event.ExceptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CategoryEventProducer categoryEventProducer;
    public List<CategoryDto> getAllCategory(){
       return categoryRepo.findAll().stream().map(categoryEntity->new CategoryDto(categoryEntity.getId(),categoryEntity.getName(),categoryEntity.getDescription())).toList();
    }

    public CategoryDto getCategoryById(Long id){
        CategoryEntity category =categoryRepo.findById(id).orElseThrow();
        return new CategoryDto(category.getId(),category.getName(),category.getDescription());
    }

    public CategoryDto createCategory(CategoryReqDto c){

        CategoryEntity categoryEntity= categoryRepo.save(new CategoryEntity(c.name(),c.description() ));
        return new CategoryDto(categoryEntity.getId(),categoryEntity.getName(),categoryEntity.getDescription());
    }
    public CategoryDto updateCategory(Long id, CategoryDto c){

        try {
            CategoryDto category = getCategoryById(id);


            CategoryEntity categoryEntity = categoryRepo.save(new CategoryEntity(id, c.name(), c.description()));
            return new CategoryDto(categoryEntity.getId(), categoryEntity.getName(), categoryEntity.getDescription());
        }
        catch (Exception e) {
            throw   new RuntimeException("record not found");
        }

    }
    public List<CategoryDto> findByName(String name) {
        List<CategoryEntity> ca= categoryRepo.findByNameIgnoreCase(name);

            return ca.stream().map(c->new CategoryDto(c.getId(), c.getName(), c.getDescription())).toList();


    }
    public ResponseEntity<String> deleteCategory(Long id) {
//        CategoryEntity c= categoryRepo.findById(id).orElseThrow(()-> new NoSuchElementException("no category exists with "+id));
//
//           categoryRepo.deleteById(id);
        System.out.println("deletion service");
        categoryEventProducer.sendCategoryDeleted(id);
           return new ResponseEntity<>("successfully deleted",HttpStatus.OK);
    }
}
