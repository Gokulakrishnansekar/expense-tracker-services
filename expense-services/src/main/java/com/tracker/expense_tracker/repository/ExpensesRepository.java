package com.tracker.expense_tracker.repository;

import com.tracker.expense_tracker.Entity.Expense;
import com.tracker.shared_services.DTO.AmountByCategory;
import com.tracker.shared_services.kafka.constants.Status;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expense,Long> , JpaSpecificationExecutor<Expense> {



    @Query("select sum(e.amount) as totalAmount,e.category_id as category from Expense" +
            " e where (:userName is null or e.userName like (:userName)) " +
            "and (:startDate is null or e.date>= :startDate) " +
            "and (:endDate is null or e.date<= :endDate) " +
            "group by e.category_id")
    public List<AmountByCategory> getAmountByCategory(@Param("userName") String username,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    @Modifying
    @Query("update Expense e set e.status=:status,e.lastModifiedDate= :lastModifiedDate where e.category_id = :id ")
    void updateDelete(@Param("id") Long id, Status status ,@Param("lastModifiedDate") LocalDate lastModifiedDate);


    @Modifying
    @Query("delete  from Expense e where e.lastModifiedDate <=:cutoff and e.status =:status")
    public void deleteExpenseAfterCutoff(Status status, LocalDate cutoff);

}



