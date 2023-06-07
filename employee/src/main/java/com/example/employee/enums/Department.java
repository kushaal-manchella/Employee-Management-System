package com.example.employee.enums;

public enum Department {
	ENGINEERING("engineering"),
	FINANCE("finance"),
	HR("hr");
	
	private final String name;
	
	Department(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
    public static Department fromString(String value) {
        for (Department department : Department.values()) {
            if (department.name.equalsIgnoreCase(value)) {
                return department;
            }
        }
        throw new IllegalArgumentException("Invalid department value: " + value);
    }
}