package com.service.category_service.service;

import com.service.category_service.repository.CategoryRepo;
import com.tracker.shared_services.kafka.constants.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DbTransaction {
    private final CategoryRepo categoryRepo;

    @Transactional
    public void updateStatus(long id, Status status){
        categoryRepo.updateStatus(id,Status.Deletion_pending);
    }
}
