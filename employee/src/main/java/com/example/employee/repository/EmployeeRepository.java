package com.example.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.employee.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Query(value = "SELECT SUM(e.salary) FROM Employee e WHERE e.department = ?1")
	int getSalarySumByDepartment(String department);
	
	@Query(value = "SELECT * FROM Employee e WHERE e.reporting_manager = ?1", nativeQuery = true)
	List<Employee> getReportersOfManager(Long reportingManager);
	
	@Query(value = "SELECT * FROM Employee e WHERE e.department = ?1 AND e.reporting_manager is null", nativeQuery = true)
	List<Employee> getDepartmentHeads(String department);
	
	@Query(value = "SELECT * FROM Employee e WHERE e.employee_id = ?1 AND e.role = 'manager'", nativeQuery = true)
	Optional<Employee> isEmployeeManager(Long employeeId);
	
	@Query(value = "SELECT * FROM Employee e Where e.department = ?1", nativeQuery = true)
	List<Employee> getEmployeesByDepartment(String department);
	
}


