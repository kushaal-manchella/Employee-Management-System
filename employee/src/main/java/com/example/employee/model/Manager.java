package com.example.employee.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.sql.Date;

@Entity
@DiscriminatorValue("manager")
public class Manager extends Employee{
	public Manager() {};
	
	public Manager(Long employeeId, String name, Date dob, String email, int salary, Long reportingManager, String department) {
		super.setEmployeeId(employeeId);
		super.setName(name);
		super.setDob(dob);
		super.setEmailId(email);
		super.setSalary(salary);
		super.setReportingManager(reportingManager);
		super.setDepartment(department);
	}
}
