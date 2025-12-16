package com.tracker.expense_tracker.Entity;

import com.tracker.shared_services.kafka.constants.Status;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.metrics.Stat;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="expenses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @NotBlank(message = "title should not be empty")
    @Size(min = 4,max = 30,message = "title should between 4 - 30 chars")
    @Column(name="title")
    private String title;

    @NotNull(message = "should not be zero")
    @Column(name="amount")
    private Double amount;

    @Column(name="date")
    @NotNull(message = "date is important field")
    @PastOrPresent(message = "date should not be future")
    private LocalDate date;



    @NotBlank(message = "user name  should not be empty")
    @Column(name="userName")
    private String userName;

    @NotNull(message = "category id  should not be empty")
    @Column(name="category_id")
    private Long category_id;

    @Column(name="created_date")
    @CurrentTimestamp()
    private String createDate;

    @Column(name="last_modified_date")
    private LocalDate  lastModifiedDate;

    @Column(name="status")
    @Enumerated( EnumType.STRING)
    private Status status=Status.Active;

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", userName='" + userName + '\'' +
                ", category_id=" + category_id +
                ", createDate='" + createDate + '\'' +
                '}';
    }

}
