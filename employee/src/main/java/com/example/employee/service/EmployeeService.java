package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.employee.enums.Department;
import com.example.employee.model.Developer;
import com.example.employee.model.Employee;
import com.example.employee.model.Manager;
import com.example.employee.model.Qatester;
import com.example.employee.repository.EmployeeRepository;
import com.example.exceptions.InvalidInputException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
	@Autowired
		EmployeeRepository empRepository;
	
	@PostMapping
	public ResponseEntity<Employee> createEmployee(Employee requestBody) {
		
		// validate the requests 
		requestBody.validate();
		
		// reportingManager validation 
		if (requestBody.getReportingManager() != null)
		{
			if (!empRepository.existsById(requestBody.getReportingManager())) {
				throw new InvalidInputException("The reportingManager of provided Id does not exist in the table");
			}
			else if (!sameDepartmentAsManager(requestBody.getDepartment(), requestBody.getReportingManager())) {
				throw new InvalidInputException("This employee cannot be in a different department than their manager");
			}
		}
		
		// create the right object depending on the role
		if ("developer".equals(requestBody.getRole())) {
			requestBody = new Developer();
			empRepository.save(requestBody);
			return new ResponseEntity<>(requestBody, HttpStatus.CREATED);
		} else if ("qatester".equals(requestBody.getRole())) {
			requestBody = new Qatester();
			empRepository.save(requestBody);
			return new ResponseEntity<>(requestBody, HttpStatus.CREATED);
		} else if ("manager".equals(requestBody.getRole())) {
			requestBody = new Manager();
			empRepository.save(requestBody);
			return new ResponseEntity<>(requestBody, HttpStatus.CREATED);
		} else {
			throw new InvalidInputException("Invalid role. Role must be either manager, developer or qatester");
		}
		
	}
	
	public ResponseEntity<Employee> updateEmployee(Long employeeId, Employee requestBody) {
		
		// validate the request
		requestBody.validate();
		
		Employee emp = empRepository.findById(employeeId).get();

		// reporting manager validation
		if (requestBody.getReportingManager() != null)
		{
			if (!empRepository.existsById(requestBody.getReportingManager())) {
				throw new InvalidInputException("The reportingManager of provided Id does not exist in the table");
			}
			else if (!sameDepartmentAsManager(requestBody.getDepartment(), requestBody.getReportingManager())) {
				throw new InvalidInputException("This employee cannot be in a different department than their manager");
			}
		}
		
		// update existing record 
		emp.setName(requestBody.getName());
		emp.setDob(requestBody.getDob());
		emp.setEmailId(requestBody.getEmailId());
		emp.setSalary(requestBody.getSalary());
		emp.setReportingManager(requestBody.getReportingManager());
		emp.setDepartment(requestBody.getDepartment());

		empRepository.save(emp);
		
		return new ResponseEntity<>(emp, HttpStatus.OK);				
	}

	public void deleteEmployee(Long employeeId) {
		if (!empRepository.existsById(employeeId)) {
			throw new InvalidInputException("Employee with the provided employeeId does not exist");
		}
		
		if(empRepository.getReportersOfManager(employeeId) != null) {
			throw new InvalidInputException("Cannot remove employee who has direct reports");
		}
	    empRepository.deleteById(employeeId);
	}
	
	public ResponseEntity<Map<String, Object>> getEmployeeHierarchy(Department department) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> hierarchy = new HashMap<>();

		response.put("department", department.getName());
		response.put("hierarchy", hierarchy);

		List<Employee> departmentHeads = empRepository.getDepartmentHeads(department.getName());

		for(Employee e:departmentHeads) {
			getEmployeesInHierarchy(e, response.get("hierarchy"));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	private void getEmployeesInHierarchy(Employee employee, Object parentMap){ 
		HashMap<String,Object> map = (HashMap<String,Object>) parentMap;
		map.put(employee.getName(), new HashMap<String, Object>());
		
		
		if(empRepository.isEmployeeManager(employee.getEmployeeId()).isPresent())
		{
			List<Employee> directReports = empRepository.getReportersOfManager(employee.getEmployeeId());
			for(Employee e: directReports) {
				getEmployeesInHierarchy(e, map.get(employee.getName()));
			}
		}
		
		return;
	}
	
	private boolean sameDepartmentAsManager(String department, Long reportingManager) {
		Employee manager = empRepository.findById(reportingManager).get();
		
		if (!manager.getDepartment().equals(department)) {
			return false;
		}
		return true;
		
	}

}
