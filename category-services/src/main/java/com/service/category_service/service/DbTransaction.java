package com.service.category_service.service;

import com.service.category_service.repository.CategoryRepo;
import com.tracker.shared_services.kafka.constants.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DbTransaction {
    private final CategoryRepo categoryRepo;

    @Transactional
    public void updateStatus(long id, Status status){
        categoryRepo.updateStatus(id,status, LocalDate.now());
    }

    @Transactional
    @Scheduled(cron = "0 0 9 1 * *")
    public  void cleanDeletedCategory(){
        System.out.println(LocalDate.now().minusDays(1));
        categoryRepo.deleteOldSoftDeleted(Status.Deleted,LocalDate.now().minusDays(1));
    }
}
