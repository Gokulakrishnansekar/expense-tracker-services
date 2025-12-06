package com.tracker.expense_tracker.specification;

import com.tracker.expense_tracker.Entity.Expense;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ExpenseSpecification  {

    public  static Specification<Expense> filterBy(ExpenseSpecificationFilter expenseSpecificationFilter){
        return (root ,query,cb)->{
            List<Predicate> predicate=new ArrayList<>();

            if(expenseSpecificationFilter.getCategory()!=null && !expenseSpecificationFilter.getCategory().isEmpty()){

                predicate.add(cb.like(cb.lower(root.get("category")),"%"+ expenseSpecificationFilter.getCategory().toLowerCase()+"%"));
            }
            if(expenseSpecificationFilter.getMinAmount()!=null && expenseSpecificationFilter.getMaxAmount()!=null){

                predicate.add(cb.between(root.get("amount"),expenseSpecificationFilter.getMinAmount(),expenseSpecificationFilter.getMaxAmount()));
            }
            if(expenseSpecificationFilter.getUserName()!=null && !expenseSpecificationFilter.getUserName().isEmpty()){
                predicate.add(cb.like(cb.lower(root.get("userName")),"%"+expenseSpecificationFilter.getUserName().toLowerCase()+"%"));
            }

            return cb.and(predicate.toArray(new Predicate[0]));
        };
        }
    }

