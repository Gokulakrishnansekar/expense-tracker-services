package com.tracker.expense_tracker.service;

import com.tracker.expense_tracker.repository.ExpensesRepository;
import com.tracker.shared_services.kafka.constants.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DbTransaction {
    private final ExpensesRepository expensesRepository;
    @Transactional
    public void updateExpenseDelete(long id, Status status){
        expensesRepository.updateDelete(id,status);

    }
}
