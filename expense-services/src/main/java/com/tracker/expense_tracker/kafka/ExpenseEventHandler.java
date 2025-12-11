package com.tracker.expense_tracker.kafka;
import com.tracker.expense_tracker.service.ExpenseService;
import com.tracker.shared_services.kafka.CategoryDeleteEvent;
import com.tracker.shared_services.kafka.constants.Topics;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseEventHandler {

    @Autowired
    ExpenseService expenseService;

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @KafkaListener(topics = Topics.category_deletion_started,groupId = "expense-service-group")
    public void handleCategoryDeleted(CategoryDeleteEvent event) {
        System.out.println("Received Category Delete event in expense service with id" + event.getId());

        try{
            throw new Exception("exception");
           // kafkaTemplate.send(Topics.expense_deletion_succeeded,event);
           // System.out.println("deletion success");
        }catch (Exception e){
            System.out.println("deletion failed");
            kafkaTemplate.send(Topics.expense_deletion_failed,event);
        }

    }


}
