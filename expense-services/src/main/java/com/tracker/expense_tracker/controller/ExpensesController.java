package com.tracker.expense_tracker.controller;


import com.tracker.shared_services.DTO.*;
import com.tracker.expense_tracker.service.ExpenseService;
import com.tracker.expense_tracker.specification.ExpenseSpecificationFilter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {
    public  final ExpenseService expenseService;
    public ExpensesController( ExpenseService expenseService){
        this.expenseService=expenseService;
    }

    //@Cacheable("expensesCache")-->if key is not needed
    @GetMapping("/")
    @Operation(summary = "get Expenses",description = "Getting all Expenses")
    public PageView<ExpenseResponseDTO> getExpenses(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false ,defaultValue = "id") String sortBy,
            @RequestParam(required = false , defaultValue = "asc") String sortDirection,
            @ModelAttribute ExpenseSpecificationFilter expenseSpecificationFilter
    ){
        Sort s=null;
        if(sortDirection.equalsIgnoreCase("ASC"))
        {
            s=Sort.by(sortBy).ascending();
        }
        else if(sortDirection.equalsIgnoreCase("DESC")){
            s=Sort.by(sortBy).descending();
        }
        assert s != null;
        Pageable p= PageRequest.of(page-1,size,s);
        return expenseService.getAllExpense(expenseSpecificationFilter,p);
    }


    @Cacheable(value = "expense" ,key = "#id")
    @GetMapping("/{id}")
    public ExpenseResponseDTO getExpensesById(@PathVariable Long id){
            return expenseService.getExpenseById(id);
    }

    @PostMapping("/expenses")
    public ExpenseResponseDTO getExpenses(@Valid @RequestBody ExpenseRequestDTO e){
        return expenseService.addExpense(e);
    }

    @CachePut(key = "#id",value = "expense")
    @PutMapping("/{id}")
    public ExpenseResponseDTO getExpenses(@Valid @RequestBody ExpenseRequestDTO e,@PathVariable Long id){
        return expenseService.updateExpense(e,id);
    }


    @Operation(description = "get category")
    @GetMapping("category/{id}")
    public CategoryDto todo(@PathVariable Long id)
    {

        return expenseService.getCategoryById(id);
    }



    @GetMapping("/summary/category")
    public List<AmountByCategory> GetAmountByCategory(@RequestParam(required = false) String userName,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat LocalDate startDate,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat LocalDate endDate

                                                      ){
            return expenseService.getAmountByCategory(userName,startDate,endDate);
    }


}

