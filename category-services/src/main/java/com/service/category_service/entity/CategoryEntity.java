package com.service.category_service.entity;

import com.tracker.shared_services.kafka.constants.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoryEntity {
    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CategoryEntity(long id, String name, String description, LocalDate lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lastModifiedDate=lastModifiedDate;
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name="status")
    @Enumerated( EnumType.STRING)
    private Status status=Status.Active;

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;
}
