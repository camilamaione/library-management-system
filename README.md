# library-management-system

## Description
This is a Java implementation of the library management software described in https://www.javatpoint.com/library-management-system-in-java-swing. Some modifications were done to the original design:

- There are originally two types of users that can log into the software: a common librarian, which can view and add books and manage issues, and an administrator who can add, view and delete librarians. In this implementation, administrator is an attribute of a librarian and the librarian who is marked as administrator can add, view and delete librarians in addition to the default permissions. 
- In this implementation, librarians can also remove and edit books and issues.
- The issuedbooks table was modified. Instead of holding itself the data of the students who received books, now this table stores a foreign key to a new table named student, which stores the students' data (name, e-mail, contact...). 

## Implementation details

Language: Java 11

GUI: JavaFX 11 with MVC as architetural pattern

Database connectivity: JDBC for MySQL

Design patterns used: Data Access Objects, Factory (for creating data access objects) and Observer (for notifications between views)

Software fully documented in Javadoc.
