package com.example.employee.service;
import java.sql.Date;

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
import com.example.employee.requests.EmployeeRequest;
import com.example.exceptions.InvalidInputException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
	@Autowired
		EmployeeRepository empRepository;
	
	@PostMapping
	public ResponseEntity<Employee> createEmployee(EmployeeRequest requestBody) {
		// get employee details through requestBody 
		Long employeeId = requestBody.getEmployeeId();
		String name = requestBody.getName();
		Date dob = requestBody.getDob();
		String emailId = requestBody.getEmailId();
		int salary = requestBody.getSalary();
		Long reportingManager = requestBody.getReportingManager();
		String department = requestBody.getDepartment();
		String role = requestBody.getRole();
		
		// validate the requests 
		if (reportingManager == null) {
			validateRequest(name, dob, emailId, salary, department, role);
		}
		else {
			validateRequest(name, dob, emailId, salary, reportingManager, department, role);
		}
		
		// create the right object depending on the role
		if ("developer".equals(role)) {
			Developer newDev = new Developer(employeeId, name, dob, emailId, salary, reportingManager, department);
			empRepository.save(newDev);
			return new ResponseEntity<>(newDev, HttpStatus.CREATED);
		} else if ("qatester".equals(role)) {
			Qatester newTester = new Qatester(employeeId, name, dob, emailId, salary, reportingManager, department);
			empRepository.save(newTester);
			return new ResponseEntity<>(newTester, HttpStatus.CREATED);
		} else if ("manager".equals(role)) {
			Manager newManager = new Manager(employeeId, name, dob, emailId, salary, reportingManager, department);
			empRepository.save(newManager);
			return new ResponseEntity<>(newManager, HttpStatus.CREATED);
		} else {
			throw new InvalidInputException("Invalid role. Role must be either manager, developer or qatester");
		}
		
	}
	
	public ResponseEntity<Employee> updateEmployee(Long employeeId, EmployeeRequest requestBody) {
		if (!"developer".equals(requestBody.getRole()) && !"qatester".equals(requestBody.getRole()) && !"manager".equals(requestBody.getRole())) {
			throw new InvalidInputException("Invalid role. Role must be either manager, developer or qatester");
		}
		Employee emp = empRepository.findById(employeeId).get();
		
		if (!sameDepartmentAsManager(requestBody.getDepartment(), requestBody.getReportingManager())) {
			throw new InvalidInputException("This employee cannot be in a different department than their manager");
		}

		// only set the parameters that were passed in through the body. Ignore null values
		if (requestBody.getName() != null) {
			emp.setName(requestBody.getName());
		}
		if (requestBody.getDob() != null) {
			emp.setDob(requestBody.getDob());
		}
		if (requestBody.getEmailId() != null) {
			emp.setEmailId(requestBody.getEmailId());
		}
		if (requestBody.getSalary() != 0) {
			emp.setSalary(requestBody.getSalary());
		}
		if (requestBody.getReportingManager() != null) {
			emp.setReportingManager(requestBody.getReportingManager());
		}
		if (requestBody.getDepartment() != null) {
			emp.setDepartment(requestBody.getDepartment());
		}

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
	
	protected void getEmployeesInHierarchy(Employee employee, Object parentMap){ 
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
	
	// Validations should ideally be in model classes but repository is being used in these which might be a good indication to leave them in the service class 
	private void validateRequest(String name, Date dob, String emailId, int salary, String department, String role) {
		if(name == null) {
			throw new InvalidInputException("Please provide a name field");
		}
		else if (dob == null) {
			throw new InvalidInputException("Please provide a dob field");
		}
		else if (emailId == null) {
			throw new InvalidInputException("Please provide an emailId field");
		}
		else if (salary == 0) {
			throw new InvalidInputException("Please provide a salary field");
		}
		else if (department == null) {
			throw new InvalidInputException("Please provide a department field");
		}
		else if (!"manager".equals(role) && !"qatester".equals(role) && !"developer".equals(role)) {
			throw new InvalidInputException("Invalid role. Role must be either manager, developer or qatester");
		}
	}
	
	private void validateRequest(String name, Date dob, String emailId, int salary, Long reportingManager, String department, String role) {
		if(name == null) {
			throw new InvalidInputException("Please provide a name field");
		}
		else if (dob == null) {
			throw new InvalidInputException("Please provide a dob field");
		}
		else if (emailId == null) {
			throw new InvalidInputException("Please provide an emailId field");
		}
		else if (salary == 0) {
			throw new InvalidInputException("Please provide a salary field");
		}
		else if (department == null) {
			throw new InvalidInputException("Please provide a department field");
		}
		else if (reportingManager == null) {
			throw new InvalidInputException("Please provide a reportingManager field");
		}
		else if (!empRepository.existsById(reportingManager)) {
			throw new InvalidInputException("The reportingManager of provided Id does not exist in the table");
		}
		else if (!sameDepartmentAsManager(department, reportingManager)) {
			throw new InvalidInputException("This employee cannot be in a different department than their manager");
		}
		else if (!"manager".equals(role) && !"qatester".equals(role) && !"developer".equals(role)) {
			throw new InvalidInputException("Invalid role. Role must be either manager, developer or qatester");
		}
	}
	
	private boolean sameDepartmentAsManager(String department, Long reportingManager) {
		Employee manager = empRepository.findById(reportingManager).get();
		
		if (!manager.getDepartment().equals(department)) {
			return false;
		}
		return true;
		
	}

}
