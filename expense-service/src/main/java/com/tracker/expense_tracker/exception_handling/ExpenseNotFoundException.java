package com.tracker.expense_tracker.exception_handling;

public class ExpenseNotFoundException extends RuntimeException{
    public ExpenseNotFoundException(){
        super("Expense is not found in Database");
    }

}
