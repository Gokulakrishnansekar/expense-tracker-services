package com.tracker.expense_tracker.exception_handling;


import com.tracker.shared_services.DTO.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex){
        //HashMap<String,String> errors=new HashMap<>();
       ErrorResponse err=new ErrorResponse();
       err.setError(ex.getMessage());
       err.setTimestamp(System.currentTimeMillis());
       err.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlingGlobalException(Exception ex){

        ErrorResponse err=new ErrorResponse();
        err.setError(ex.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        err.setStatus(HttpStatus.BAD_REQUEST);


        return  new ResponseEntity<>(err, HttpStatusCode.valueOf(400));


    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(ExpenseNotFoundException ex){
        ErrorResponse err=new ErrorResponse();
        err.setError(ex.getMessage());
        err.setTimestamp(System.currentTimeMillis());
        err.setStatus(HttpStatus.NOT_FOUND);
        return  new ResponseEntity<>(err, HttpStatusCode.valueOf(404));
    }




}
