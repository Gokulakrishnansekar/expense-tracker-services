package com.service.category_service.kafka_producer;

import com.service.category_service.entity.CategoryEntity;
import com.service.category_service.repository.CategoryRepo;
import com.service.category_service.service.CategoryService;
import com.service.category_service.service.DbTransaction;
import com.tracker.shared_services.kafka.CategoryDeleteEvent;
import com.tracker.shared_services.kafka.constants.Status;
import com.tracker.shared_services.kafka.constants.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryEventHandler {

private final KafkaTemplate<String,Object> kafkaTemplate;
private  final DbTransaction dbTransaction;

@Transactional()
    public  void sendCategoryDeleted(Long categoryId, CategoryEntity c) {
        CategoryDeleteEvent event = new CategoryDeleteEvent(categoryId);


        try{
            dbTransaction.updateStatus(categoryId,Status.Deletion_pending);
            kafkaTemplate.send(Topics.category_deletion_started,event);
            log.info("send kafka {}  topic with id{}",Topics.category_deletion_started, event.getId());

        }catch (Exception e)
        {
            throw new RuntimeException( e.getMessage());
        }
    }
    @Transactional()
    @KafkaListener(topics = {Topics.category_deletion_failed,Topics.expense_deletion_failed},groupId = "category-service-group")
    public void deleteCategoryFailed(CategoryDeleteEvent event) {
        log.info("received kafka category_deletion_failed topic with id{}", event.getId());
        try {
            dbTransaction.updateStatus(event.getId(), Status.Active);
            log.info("roll back success in category");
           // return ResponseEntity.internalServerError().body("roll back success in category");

        } catch (Exception e) {
         log.info("roll back failed in category"+ e.getMessage());
         throw e;
            // ResponseEntity.internalServerError().body("roll back failed in category");
        }
    }

    @Transactional()
    @KafkaListener(topics = Topics.expense_deletion_started,groupId = "category-service-group")
    public void deleteCategory(CategoryDeleteEvent event) {
        log.info("listen to kafka topic {} with id {}",Topics.expense_deletion_started,event.getId());
        try {
            dbTransaction.updateStatus(event.getId(),Status.Deleted);
            kafkaTemplate.send(Topics.category_deletion_succeeded, event);
            log.info("send to kafka topic {} with id {}",Topics.category_deletion_succeeded,event.getId());
        } catch (Exception e) {
            log.info("send to kafka topic {} with id {}",Topics.category_deletion_failed,event.getId());
            kafkaTemplate.send(Topics.category_deletion_failed, event);
        }
    }

}
