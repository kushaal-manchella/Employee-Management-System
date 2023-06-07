package com.example.employee.model;

import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("manager")
public class Manager extends Employee{
	public Manager() {
		super.setRole("manager");
	};
	
}
