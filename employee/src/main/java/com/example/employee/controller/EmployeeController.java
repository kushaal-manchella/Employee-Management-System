package com.example.employee.controller;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.example.employee.enums.Department;
import com.example.employee.model.Employee;
import com.example.employee.service.CostAllocationService;
import com.example.employee.service.EmployeeService;
import com.example.exceptions.InvalidInputException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;


@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    EmployeeService empService;
    
    @Autowired
    CostAllocationService costAllocationService;
    
	@RequestMapping(value="/employees", method=RequestMethod.POST)
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee emp) {
	    return empService.createEmployee(emp);
	}
	
	@RequestMapping(value="/employees/{employeeId}", method=RequestMethod.PUT)
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "employeeId") Long id, @RequestBody Employee emp) {
	    return empService.updateEmployee(id, emp);
	}
	
	@RequestMapping(value="/employees/{employeeId}", method=RequestMethod.DELETE)
	public void deleteEmployees(@PathVariable(value = "employeeId") Long id) {
	    empService.deleteEmployee(id);
	}
	
	@RequestMapping(value="/department/cost_allocation/{department}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> departmentCostAllocation(@PathVariable(value = "department") String department) {
		Department inputDepartment = Department.fromString(department);
		return costAllocationService.getCostAllocation(inputDepartment);
	}
	
	@RequestMapping(value="/manager/cost_allocation/{employeeId}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> managerCostAllocation(@PathVariable(value = "employeeId") Long id) {
		return costAllocationService.getCostAllocation(id);
	}
	
	@RequestMapping(value="/department/hierarchy/{department}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getEmployeesByDepartment(@PathVariable String department) {
		Department inputDepartment = Department.fromString(department);
		return empService.getEmployeeHierarchy(inputDepartment);
	}
	
	@ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidRoleException(InvalidInputException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidDepartmentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
	
	// Look for a cleaner approach to exposing error message to client
	@ControllerAdvice
	public class RestExceptionHandler {

	    @ExceptionHandler(NoSuchElementException.class)
	    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
	        return new ResponseEntity<>("Could not find employee with provided employeeID" , HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(HttpMessageNotReadableException.class)
	    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
	        Throwable cause = ex.getCause();
	        if (cause instanceof InvalidTypeIdException) {
	            InvalidTypeIdException ite = (InvalidTypeIdException) cause;
	            String message = ite.getMessage();
	            return ResponseEntity.badRequest().body(Collections.singletonMap("error", message));
	        }
	        return ResponseEntity.badRequest().build();
	    }    
	}
	
}

