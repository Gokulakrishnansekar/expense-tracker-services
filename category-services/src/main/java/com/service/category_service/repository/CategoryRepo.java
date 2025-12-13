package com.service.category_service.repository;

import com.service.category_service.entity.CategoryEntity;
import com.tracker.shared_services.kafka.constants.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {

    @Query("select e from CategoryEntity e where lower(e.name) like concat('%',lower(:name),'%')")
    List<CategoryEntity> findByNameIgnoreCase(@Param("name") String name);

    @Modifying
    @Query("update CategoryEntity c set c.status= :status where c.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") Status status);
}


