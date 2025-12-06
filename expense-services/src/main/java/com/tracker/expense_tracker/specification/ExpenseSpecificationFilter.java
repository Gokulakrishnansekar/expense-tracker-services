package com.tracker.expense_tracker.specification;


import lombok.Data;

@Data
public class ExpenseSpecificationFilter {
    private Long minAmount;
    private  Long maxAmount;
    private  String category;
    private String userName;
}
