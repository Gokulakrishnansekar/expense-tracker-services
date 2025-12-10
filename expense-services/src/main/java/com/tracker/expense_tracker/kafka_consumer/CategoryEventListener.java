package com.tracker.expense_tracker.kafka_consumer;
import com.tracker.expense_tracker.service.ExpenseService;
import com.tracker.shared_services.kafka.CategoryDeleteEvent;
import com.tracker.shared_services.kafka.constants.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CategoryEventListener {

    @Autowired
    ExpenseService expenseService;

    @KafkaListener(topics = Topics.category_deleted_topic,groupId = "expense-service-group")
    public void handleCategoryDeleted(CategoryDeleteEvent event) {
        System.out.println("Received Category Delete event: in expense service" + event.getId());
       // deleteExpensesByCategory(event.getId());
    }
}
