package com.tracker.expense_tracker.service;


import com.tracker.expense_tracker.exception_handling.ExpenseNotFoundException;
import com.tracker.expense_tracker.Entity.Expense;
import com.tracker.shared_services.DTO.*;
import com.tracker.expense_tracker.model.CategoryClient;
import com.tracker.expense_tracker.repository.ExpensesRepository;
import com.tracker.expense_tracker.specification.ExpenseSpecification;
import com.tracker.expense_tracker.specification.ExpenseSpecificationFilter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ExpenseService {

    private  final CategoryClient client;
//    Expense e1=new Expense(1L,"name",100, LocalDate.now(),"furniture","gokul");
//    Expense e2=new Expense(2L,"two",200, LocalDate.now(),"toys","abi");
//    public Map<Long,Expense> expenseRepo=new HashMap<>();

    ExpensesRepository expensesRepository;
    public ExpenseService(CategoryClient client, ExpensesRepository expensesRepository){
        this.client = client;
        this.expensesRepository=expensesRepository;
//        expenseRepo.put(1L,e1);
//        expenseRepo.put(2L,e2);
      //  expensesRepository
    }

    public PageView<ExpenseResponseDTO> convertPageDateToPageObject(Page<Expense> data){
            PageView<ExpenseResponseDTO> pv=new PageView<>();
            pv.setItems(data.getContent().stream().map(this::convertToExpenseDTO).toList());
            pv.setSize(data.getSize());
            pv.setHasNextPage(data.hasNext());
            pv.setTotalElements(data.getTotalElements());
        pv.setCurrentPageElements(data.getNumberOfElements());
           return pv;
    }

    public List<ExpenseResponseDTO> convertToExpensesDTO(List<Expense> ex)
    {


      return  ex.stream().map((expense)->{
          CategoryDto c=this.getCategoryById(expense.getCategory_id());
          System.out.println(c.toString());
          CategoryWithIdAndName categoryWithIdAndName=new CategoryWithIdAndName(c.id(),c.name());
          ExpenseResponseDTO dto=new ExpenseResponseDTO();
            dto.setId(expense.getId());
            dto.setCategory(categoryWithIdAndName);
            dto.setTitle(expense.getTitle());
            dto.setUserName(expense.getUserName());
            dto.setDate(expense.getDate());
            dto.setAmount(expense.getAmount());
            return dto;
      }).toList();
    }
    public ExpenseResponseDTO convertToExpenseDTO(Expense expense)
    {
        ExpenseResponseDTO dto=new ExpenseResponseDTO();
        CategoryDto c=this.getCategoryById(expense.getCategory_id());
        System.out.println(c.toString());
        CategoryWithIdAndName categoryWithIdAndName=new CategoryWithIdAndName(c.id(),c.name());
            dto.setId(expense.getId());
            dto.setCategory(categoryWithIdAndName);
            dto.setTitle(expense.getTitle());
            dto.setUserName(expense.getUserName());
            dto.setDate(expense.getDate());
            dto.setAmount(expense.getAmount());
            return dto;

    }
    public Expense convertToExpenseEntity(ExpenseRequestDTO expenseDto)
    {
        Expense expense=new Expense();
        expense.setCategory_id(expenseDto.getCategory_id());
        expense.setTitle(expenseDto.getTitle());
        expense.setUserName(expenseDto.getUserName());
        expense.setDate(expenseDto.getDate());
        expense.setAmount(expenseDto.getAmount());
           return expense;
    }



    public PageView<ExpenseResponseDTO> getAllExpense(ExpenseSpecificationFilter expenseSpecificationFilter, Pageable p){
        Specification<Expense> spe=ExpenseSpecification.filterBy(expenseSpecificationFilter);
        Page<Expense> data=expensesRepository.findAll(spe,p);
       return  this.convertPageDateToPageObject(data);



    }

    public ExpenseResponseDTO getExpenseById(Long l)
    {

       Expense e=  expensesRepository.findById(l).orElseThrow(ExpenseNotFoundException::new);
         return  convertToExpenseDTO(e);
    }

    public ExpenseResponseDTO addExpense(ExpenseRequestDTO e){
        try{
            System.out.println(convertToExpenseEntity(e).toString());
          Expense saved=  expensesRepository.save(convertToExpenseEntity(e));
            return this.convertToExpenseDTO(saved);
        }
        catch (Exception exception){
            System.out.println("error while saving"+ exception.getMessage());
            return null;
        }

    }

    public ExpenseResponseDTO updateExpense(ExpenseRequestDTO e,Long id){

             expensesRepository.findById(id).orElseThrow(ExpenseNotFoundException::new);
             Expense expense=this.convertToExpenseEntity(e);
             expense.setId(id);
        System.out.println(expense.toString());
             Expense saved=  expensesRepository.save(expense);
             return this.convertToExpenseDTO(saved);



    }

    public List<AmountByCategory> getAmountByCategory(String userName, LocalDate startDate,LocalDate endDate){
        return expensesRepository.getAmountByCategory(userName,startDate,endDate);
    }

    @CircuitBreaker(name = "category-service", fallbackMethod = "serverDown")
    public CategoryDto getCategoryById(Long id)
    {
      //  CategoryClient client = null;
    return client.getCategoryById(id);

    }
    public CategoryDto serverDown(Long l,Throwable t){
        log.error("server is down");
        throw new RuntimeException("server is down");
    }

}
