package com.tracker.expense_tracker.kafka;
import com.tracker.expense_tracker.repository.ExpensesRepository;
import com.tracker.expense_tracker.service.ExpenseService;
import com.tracker.shared_services.kafka.CategoryDeleteEvent;
import com.tracker.shared_services.kafka.constants.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseEventHandler {


   private final ExpenseService expenseService;
    private final ExpensesRepository expensesRepository;

    private final KafkaTemplate<String,Object> kafkaTemplate;
    @Transactional()
    @KafkaListener(topics = Topics.category_deletion_started,groupId = "expense-service-group")
    public void handleCategoryDeletionStart(CategoryDeleteEvent event) {
        log.info("listen to kafka topic {} with id {}",Topics.category_deletion_started,event.getId());

        try{
            expensesRepository.updatePartialDelete(event.getId());
            log.info("send to kafka topic {} with id {}",Topics.expense_deletion_started,event.getId());
            kafkaTemplate.send(Topics.expense_deletion_started,event);

        }catch (Exception e){
            log.info("send to kafka topic {} with id {}",Topics.expense_deletion_failed,event.getId());
            kafkaTemplate.send(Topics.expense_deletion_failed,event);
        }

    }
    @Transactional()
    @KafkaListener(topics = Topics.category_deletion_succeeded,groupId = "expense-service-group")
    public void handleCategoryDeletionSuccess(CategoryDeleteEvent event) {
        log.info("listen to kafka topic {} with id {}",Topics.category_deletion_succeeded,event.getId());

        try{
            expensesRepository.updateExpenseDelete(event.getId());
            log.info("send to kafka topic {} with id {}",Topics.expense_deletion_succeeded,event.getId());
            kafkaTemplate.send(Topics.expense_deletion_succeeded,event);
            log.warn("-------------------------------------------------------------------------");

        }catch (Exception e){
            log.info("send to kafka topic {} with id {}",Topics.expense_deletion_failed,event.getId());
            kafkaTemplate.send(Topics.expense_deletion_failed,event);
        }

    }

    @Transactional()
    @KafkaListener(topics = Topics.category_deletion_failed,groupId = "expense-service-group")
    public void handleCategoryDeletionFailed(CategoryDeleteEvent event) {
        log.info("listen to kafka topic {} with id {}",Topics.category_deletion_failed,event.getId());

        try{
            expensesRepository.updateRollBackDelete(event.getId());
            log.info("send to kafka topic {} with id {}",Topics.expense_deletion_succeeded,event.getId());
            kafkaTemplate.send(Topics.expense_deletion_succeeded,event);

        }catch (Exception e){
            log.info("send to kafka topic {} with id {}",Topics.expense_deletion_failed,event.getId());
            kafkaTemplate.send(Topics.expense_deletion_failed,event);
        }

    }


    }



