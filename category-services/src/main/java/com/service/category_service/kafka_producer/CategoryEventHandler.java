package com.service.category_service.kafka_producer;

import com.tracker.shared_services.kafka.CategoryDeleteEvent;
import com.tracker.shared_services.kafka.constants.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryEventHandler {

private final KafkaTemplate<String,Object> kafkaTemplate;

    public  void sendCategoryDeleted(Long categoryId) {
        CategoryDeleteEvent event = new CategoryDeleteEvent(categoryId);
        System.out.println("category deletion started");

            kafkaTemplate.send(Topics.category_deletion_started,event);


    }

    @KafkaListener(topics = Topics.expense_deletion_succeeded,groupId = "expense-service-group")
    public void deleteCategory(CategoryDeleteEvent event){
        log.info("received kafka expense_deletion_succeeded topic with id" +event.getId());

    }
    @KafkaListener(topics = Topics.expense_deletion_failed,groupId = "expense-service-group")
    public void rollbackCategory(CategoryDeleteEvent event){
        log.info("received kafka expense_deletion_failed topic with id" +event.getId());


    }
}
