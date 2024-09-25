Library Management System
Overview
The Library Management System (LMS) is a Java-based application designed to manage the functionalities of a library. 
It allows users to log in, view available books, borrow and return books, and provides administrative functionalities for managing books in the library.

Features
User authentication (login/logout).
View available books.
Borrow and return books.
Admin functionalities:
Add new books.
Update existing book information.
Display all books in a formatted table.

Technologies Used
Java: The primary programming language.
MySQL: For database management.
Swing: For building the graphical user interface.

Prerequisites
Before running the application, ensure you have the following installed:

Java Development Kit (JDK) - Version 8 or higher.
MySQL - Version 5.7 or higher.
MySQL Connector/J - JDBC driver for MySQL.

Set up the database:

Create a new database in MySQL named lib1.
Run the following SQL commands to set up the necessary tables:
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('admin', 'user') NOT NULL
);

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    available BOOLEAN NOT NULL
);


You can insert some initial users and books into the users and books tables for testing.
Configure database connection:
Ensure the url, username, and password in the LibraryManagementSystem.java file match your MySQL setup.

Run the application:
Login:

Enter your username and password.
The application will redirect you to the user or admin interface based on your role.

License
This project is licensed under the MIT License - see the LICENSE file for details.

Acknowledgments
Special thanks to the contributors and the open-source community for their invaluable resources.
