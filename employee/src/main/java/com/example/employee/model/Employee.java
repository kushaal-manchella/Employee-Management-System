package com.example.employee.model;


import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;



@Entity
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Table(name = "employee")
public abstract class Employee {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employee_id")
    private Long employeeId;
    
    @Column(name="name")
    private String name;
    
    @Column(name="dob")
    private Date dob;
    
    @Column(name="email")
    private String emailId;
    
    @Column(name="salary")
    private int salary; 
    
    @Column(name="reporting_manager")
    private Long reportingManager;
    
    @Column(name="department")
    private String department;
    
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public Long getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(Long reportingManager) {
		this.reportingManager = reportingManager;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department; 
	}


}

