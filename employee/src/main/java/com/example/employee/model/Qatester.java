package com.example.employee.model;

import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("qatester")
public class Qatester extends Employee{
	public Qatester() {
		super.setRole("employee");
	};
}
