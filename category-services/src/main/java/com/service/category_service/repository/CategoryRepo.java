package com.service.category_service.repository;

import com.service.category_service.entity.CategoryEntity;
import com.tracker.shared_services.kafka.constants.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {

    @Query("select e from CategoryEntity e where lower(e.name) like concat('%',lower(:name),'%')")
    List<CategoryEntity> findByNameIgnoreCase(@Param("name") String name);

    @Modifying
    @Query("update CategoryEntity c set c.status= :status,c.lastModifiedDate= :lastModifiedDate where c.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") Status status,@Param("lastModifiedDate") LocalDate lastModifiedDate);

    @Modifying
    @Query("""
    DELETE FROM CategoryEntity c
    WHERE c.status = :status
    AND c.lastModifiedDate <= :cutoff
""")
    void deleteOldSoftDeleted(
            @Param("status") Status status,
            @Param("cutoff") LocalDate cutoff
    );


    @Query("select c from CategoryEntity c where c.status=:status")
    List<CategoryEntity> findByStatus(Status status);
}


