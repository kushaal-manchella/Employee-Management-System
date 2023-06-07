package com.example.employee.model;

import java.sql.Date;
import com.example.exceptions.InvalidInputException;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;



@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "role")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Developer.class, name = "developer"),
    @JsonSubTypes.Type(value = Manager.class, name = "manager"),
    @JsonSubTypes.Type(value = Qatester.class, name = "qatester")
})
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
    
    @Column(name="role", insertable = false, updatable = false)
    private String role;
    
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
	
    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void validate() {
		if(this.name == null) {
			throw new InvalidInputException("Please provide a name field");
		}
		else if (this.dob == null) {
			throw new InvalidInputException("Please provide a dob field");
		}
		else if (this.emailId == null) {
			throw new InvalidInputException("Please provide an emailId field");
		}
		else if (this.salary == 0) {
			throw new InvalidInputException("Please provide a salary field");
		}
		else if (this.department == null) {
			throw new InvalidInputException("Please provide a department field");
		}
		else if (!"manager".equals(this.role) && !"qatester".equals(this.role) && !"developer".equals(this.role)) {
			throw new InvalidInputException("Invalid role. Role must be either manager, developer or qatester");
		}
	}
}

