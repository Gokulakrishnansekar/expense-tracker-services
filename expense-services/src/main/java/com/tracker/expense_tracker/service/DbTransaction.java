package com.tracker.expense_tracker.service;

import com.tracker.expense_tracker.repository.ExpensesRepository;
import com.tracker.shared_services.kafka.constants.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class DbTransaction {
    private final ExpensesRepository expensesRepository;
    @Transactional
    public void updateExpenseDelete(long id, Status status){
        expensesRepository.updateDelete(id,status, LocalDate.now());

    }

    @Transactional
    @Scheduled(cron = "0 0 9 1 * *")
    public void schedulingExpenseDelete(){
        log.info("deletion started");
        expensesRepository.deleteExpenseAfterCutoff(Status.Deleted,LocalDate.now().minusDays(1));
    }
}
