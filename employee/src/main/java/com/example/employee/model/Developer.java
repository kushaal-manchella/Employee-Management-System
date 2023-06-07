package com.example.employee.model;

import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("developer")
public class Developer extends Employee{
	public Developer() {
		super.setRole("developer");
	}
}
