package com.example.employee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@DiscriminatorValue("manager")
public class Manager extends Employee{
	public Manager() {
		super.setRole("manager");
	};
	
}
