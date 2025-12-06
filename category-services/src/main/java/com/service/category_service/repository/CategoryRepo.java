package com.service.category_service.repository;

import com.service.category_service.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {

    @Query("select e from CategoryEntity e where lower(e.name) like concat('%',lower(:name),'%')")
    List<CategoryEntity> findByNameIgnoreCase(@Param("name") String name);
}
