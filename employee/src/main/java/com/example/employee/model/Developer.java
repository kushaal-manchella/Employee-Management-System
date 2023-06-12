package com.example.employee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@DiscriminatorValue("developer")
public class Developer extends Employee{
	public Developer() {
		super.setRole("developer");
	}
}
