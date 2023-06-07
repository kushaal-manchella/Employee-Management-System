package com.example.employee.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.employee.enums.Department;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.example.exceptions.InvalidInputException;

@Service
public class CostAllocationService {
	@Autowired
	EmployeeRepository empRepository;
	
	// get cost allocation by department 
	public ResponseEntity<Map<String, Object>> getCostAllocation(Department department){
		
		int costAllocation = empRepository.getSalarySumByDepartment(department.getName());
		Map<String, Object> response = new HashMap<>();
		response.put("department", department.getName());
		response.put("cost allocation", costAllocation);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	// get cost allocation by manager
	public ResponseEntity<Map<String, Object>> getCostAllocation(Long id){
		Employee emp = empRepository.findById(id).get();
		Map<String, Object> response = new HashMap<>();
		
		if(!empRepository.isEmployeeManager(id).isPresent()) {
			throw new InvalidInputException("The provided employee is not a manager");
		}
		
		// subtract the manager's salary from total since recursive call counts it
		int costAllocation = getSalaries(emp, 0) - emp.getSalary();
		
		response.put("manager", emp.getName());
		response.put("cost allocation", costAllocation);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
		
	}
	
	private int getSalaries(Employee employee, int report_salaries) {
		if (employee == null){
			return 0;
		}
		List<Employee> directReports = empRepository.getReportersOfManager(employee.getEmployeeId());
		for(Employee e: directReports) {
			report_salaries += getSalaries(e, 0);
		}
		
		return employee.getSalary() + report_salaries;
		
	}
}
