package com.example.employee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@DiscriminatorValue("qatester")
public class Qatester extends Employee{
	public Qatester() {
		super.setRole("qatester");
	};
}
