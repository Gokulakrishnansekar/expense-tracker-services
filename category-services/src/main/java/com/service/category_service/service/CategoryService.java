package com.service.category_service.service;

import com.service.category_service.dto.CategoryDto;
import com.service.category_service.dto.CategoryReqDto;
import com.service.category_service.entity.CategoryEntity;
import com.service.category_service.kafka_producer.CategoryEventHandler;
import com.service.category_service.repository.CategoryRepo;
import com.tracker.shared_services.kafka.constants.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CategoryEventHandler categoryEventHandler;
    public List<CategoryDto> getAllCategory(){
       return categoryRepo.findByStatus(Status.Active).stream().map(categoryEntity->new CategoryDto(categoryEntity.getId(),categoryEntity.getName(),categoryEntity.getDescription())).toList();
    }

    public CategoryDto getCategoryById(Long id){
        CategoryEntity category =categoryRepo.findById(id).filter(c->c.getStatus()== Status.Active).
                orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"category is not present"));
        return new CategoryDto(category.getId(),category.getName(),category.getDescription());
    }

    public CategoryDto createCategory(CategoryReqDto c){

        CategoryEntity categoryEntity= categoryRepo.save(new CategoryEntity(c.name(),c.description() ));
        return new CategoryDto(categoryEntity.getId(),categoryEntity.getName(),categoryEntity.getDescription());
    }
    public CategoryDto updateCategory(Long id, CategoryDto c){

        try {
            CategoryDto category = getCategoryById(id);



            CategoryEntity categoryEntity = categoryRepo.save(new CategoryEntity(id, c.name(), c.description(), LocalDate.now()));
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
       CategoryEntity c= categoryRepo.findById(id).orElseThrow(()-> new RuntimeException("no category exists with "+id));


        categoryEventHandler.sendCategoryDeleted(c.getId(),c);
           return new ResponseEntity<>("Started category deletion",HttpStatus.ACCEPTED);
    }
}
