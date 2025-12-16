package com.tracker.shared_services.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequestDTO {

    private String title;

    private Double amount;

    //private LocalDate date;

    private Long category_id;

    @Override
    public String toString() {
        return "ExpenseRequestDTO{" +
                "title='" + title + '\'' +
                ", amount=" + amount +
                ", category_id=" + category_id +
                ", userName='" + userName + '\'' +
                '}';
    }

    private String userName;


}
