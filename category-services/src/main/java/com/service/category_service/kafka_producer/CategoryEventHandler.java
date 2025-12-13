package com.service.category_service.kafka_producer;

import com.service.category_service.repository.CategoryRepo;
import com.service.category_service.service.CategoryService;
import com.tracker.shared_services.kafka.CategoryDeleteEvent;
import com.tracker.shared_services.kafka.constants.Status;
import com.tracker.shared_services.kafka.constants.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryEventHandler {

private final KafkaTemplate<String,Object> kafkaTemplate;
private final  CategoryRepo categoryRepo;


@Transactional()
    public  void sendCategoryDeleted(Long categoryId) {
        CategoryDeleteEvent event = new CategoryDeleteEvent(categoryId);


        try{
            categoryRepo.updateStatus(categoryId,Status.Deletion_pending);
            kafkaTemplate.send(Topics.category_deletion_started,event);
            log.info("send kafka {}  topic with id{}",Topics.category_deletion_started, event.getId());

        }catch (Exception e)
        {
            throw new RuntimeException( e.getMessage());
        }
    }
    @Transactional()
    @KafkaListener(topics = Topics.category_deletion_failed,groupId = "category-service-group")
    public void deleteCategoryFailed(CategoryDeleteEvent event) {
        log.info("received kafka category_deletion_failed topic with id{}", event.getId());
        try {
            categoryRepo.updateStatus(event.getId(), Status.Active);

        } catch (Exception e) {
            throw new RuntimeException("Rollback failed");
           // kafkaTemplate.send(Topics.category_deletion_failed, event);
        }
    }

    @Transactional()
    @KafkaListener(topics = Topics.expense_deletion_started,groupId = "category-service-group")
    public void deleteCategory(CategoryDeleteEvent event) {
        log.info("listen to kafka topic {} with id {}",Topics.expense_deletion_succeeded,event.getId());
        try {
            categoryRepo.updateStatus(event.getId(),Status.Deleted);
            kafkaTemplate.send(Topics.category_deletion_succeeded, event);
            log.info("send to kafka topic {} with id {}",Topics.category_deletion_succeeded,event.getId());
        } catch (Exception e) {
            log.info("send to kafka topic {} with id {}",Topics.category_deletion_failed,event.getId());
            kafkaTemplate.send(Topics.category_deletion_failed, event);
        }
    }
    @Transactional()
    @KafkaListener(topics = Topics.expense_deletion_failed,groupId = "category-service-group")
    public void rollbackCategory(CategoryDeleteEvent event){
        log.info("listen kafka expense_deletion_failed topic with id" +event.getId());
        try{
            categoryRepo.updateStatus(event.getId(),Status.Active);
        }
        catch (Exception e)
        {
            log.error("rollback failed");
        }
    }
}
