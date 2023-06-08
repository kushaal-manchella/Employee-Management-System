# Employee-Management-System

This spring boot project leverages REST APIs to perform CRUD operations on a MySql Database. The entry point of the application can be found in the EmployeeApplication.java file which lives in the directory: [Employee-Management-System/tree/main/employee/src/main/java/com/example/employee](https://github.com/kushaal-manchella/Employee-Management-System/tree/main/employee/src/main/java/com/example/employee). This directory also contains subdirectories which store the Controllers, Repositories, Services and Models in the application. 

Employee data is stored in one table titled “employee’ in the MySQL database. The following information is captured for each Employee:
- Name 
- Date of Birth
- Email address
- Salary
- Reporting Manager (if one exists else field is null)
- Department
- Role. (There are only 3 possible roles: developer, qatester or manager )

All fields other than reporting_manager must be non-null in the employee table.

Operations that can be performed include:
- Adding an employee
- Updating an employee 
- Removing an employee 
- Listing department structure 
- Calculating cost allocation of a department 
- Calculating cost allocation of a manager 
 
This spring-boot application uses a single-table JPA strategy which signifies that all entities will be present in a single table. The discriminator column “role” is set to enable Hibernate to differentiate between entities. 

The four entities used are:
- Employee (A public abstract class is used)
- Developer (extends Employee)
- QATester (extends Employee)
- Manager (extends Employee) 

To read more about the application visit [this link](https://docs.google.com/document/d/14AQ7QjPqkubvIWIGXDvzkTx4fUBtPw74QbIdPp7Bla0/edit?usp=sharing) and to read the unit tests visit [this link](https://docs.google.com/document/d/1fb_LvuIS2DC-7jQ128UvZzP09hQxDUyLfXGA6jDIF8k/edit?usp=sharing)
