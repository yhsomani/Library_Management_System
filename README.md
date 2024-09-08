
# Library Management System
## Overview
This Library Management System (LMS) is a Java-based application that provides functionalities for managing books, user accounts, and the issuance of books. It includes features for login, signup, viewing issued books, managing defaulters, and more.



## Features

**User Management:** 
- Users can sign up and log in to access their accounts.
**Book Management:** 
- Add, update, and delete book records.
- View issued books and manage their details.
**Pending Book Details:** 
- Display books that are issued but not yet returned.
**Defaulter List:** 
- List of students who have overdue books.
**Book Issuance and Return:** 
- Issue and return books, with tracking of due dates and status.
**Reports:** 
- View issued books, overdue books, manage defaulters, and view all records.

## Components
### `LoginPage.java`
- **Functionality:** Provides user login functionality.
- **Features:**
  - Validates user credentials.
  - Directs to the HomePage upon successful login.
- Details: Users must enter their username and password to access the system. Passwords are hashed for security.
### `SignupPage.java`
- **Functionality:** Allows new users to create an account.
- **Features:**
  - Validates user input.
  - Checks for duplicate usernames before creating a new account.
- Details: Users provide their username, password, email, and contact information to register. No password hashing is used in this implementation.
### `IssuedBookDetails.java`
- **Functionality:** Displays details of books that are currently issued and pending return.
- **Features:**
  - Retrieves and displays issued book details from the database.
  - Filters records based on the status of books (e.g., pending).
### `DefaulterList.java`
- **Functionality:** Lists students who have overdue books.
- **Features:**
  - Retrieves and displays details of students with overdue books.
  - Filters records where the due date is less than the current date.
### `HomePage.java`
- **Functionality:** Serves as the main interface for the library management system.
- **Features:**
  - Provides navigation to various functionalities such as book management, issuing books, and returning books.
### `IssueBook.java`
- **Functionality:** Manages the process of issuing books to students.
- **Features:**
  - Allows users to issue books and record the issuance details.
### `ManageBooks.java`
- **Functionality:** Manages book records in the library.
- **Features:**
  - Provides options to add, update, and delete book details.
### ` ManageStudents.java`
- **Functionality:** Manages student records.
- **Features:**
  - Provides options to add, update, and delete student details.
### `ReturnBook.java`
- **Functionality:** Manages the process of returning books.
- **Features:**
  - Allows users to return books and updates the book status accordingly.
### `ViewAllRecords.java`
- **Functionality:** Provides a comprehensive view of all records in the system.
- **Features:**
  - Displays a detailed list of all books, students, and transactions.
### `DBConnection.java`
- **Functionality:** Manages database connections.
- **Details:** Provides a method for establishing and closing connections to the database, facilitating interactions between the application and the database.
### `Error Handling`
- **Validation Errors:** Ensure all required fields are filled out correctly.
- **Database Errors:** Check database connection settings and ensure the database is running.

## 🛠 Skills Used

1. **Java**
   - **Purpose:** Core programming language used for developing the application.
   - **Application:** Implementing the backend logic and GUI components.

2. **SQL**
   - **Purpose:** Used for database interactions.
   - **Application:** Querying the database for user credentials, book details, and defaulter lists.

3. **JDBC (Java Database Connectivity)**
   - **Purpose:** Facilitates communication between Java applications and the database.
   - **Application:** Executing SQL queries and updates in the `LoginPage`, `SignupPage`, `IssuedBookDetails`, and `DefaulterList` classes.

4. **Swing (Java GUI Toolkit)**
   - **Purpose:** Provides components for building graphical user interfaces.
   - **Application:** Designing and implementing the user interface for login, signup, and displaying book details.

5. **Hashing (SHA-256)**
   - **Purpose:** Enhances security for storing and verifying user passwords.
   - **Application:** Encrypting passwords during the login process in the `LoginPage` class.

6. **Prepared Statements**
   - **Purpose:** Prevents SQL injection and improves query performance.
   - **Application:** Used in database operations for login validation and user signup.

7. **Exception Handling**
   - **Purpose:** Manages errors and exceptions gracefully.
   - **Application:** Handling database connection errors and user input validation errors.

8. **Object-Oriented Programming (OOP)**
   - **Purpose:** Structures code in a modular and reusable way.
   - **Application:** Designing classes and objects for different functionalities like login, signup, and data display.

9. **MVC (Model-View-Controller) Pattern**
   - **Purpose:** Separates application logic into different layers for better organization.
   - **Application:** Following MVC principles to manage data (Model), user interface (View), and control flow (Controller) in the application.

10. **GUI Design Principles**
    - **Purpose:** Enhances user experience and interface usability.
    - **Application:** Designing intuitive interfaces and user interactions in Swing components.

## 🛠 Skills
Java, SQL, JDBC, Swing, Hashing, Prepared Statements, Exception Handling, OOP, MVC, GUI Design
## Demo

Check out the demo of the project on YouTube:

[![Watch the video](https://yt3.ggpht.com/0lCQMjDQlZ7whz3mVGI9Go9CHEThEgVnoiG5NUG6mkWwLLaCMEB8akpuf2V7Q-Wq7-CSttZyylF3OA=s1600-rw-nd-v1)](https://youtu.be/Q9wF1ULigI0)
