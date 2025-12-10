package com.service.category_service.kafka_producer;

import com.tracker.shared_services.kafka.CategoryDeleteEvent;
import com.tracker.shared_services.kafka.constants.Topics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryEventProducer {

private final KafkaTemplate<String,Object> kafkaTemplate;

    public  void sendCategoryDeleted(Long categoryId) {
        CategoryDeleteEvent event = new CategoryDeleteEvent(categoryId);
        kafkaTemplate.send(Topics.category_deleted_topic, event);
    }
}
