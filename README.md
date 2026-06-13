# 📚 Library Management System

A desktop-based Library Management System developed using **Java Swing** and **MySQL**. This application provides librarians with an efficient way to manage books, student records, book issuance, returns, and dues through an intuitive graphical user interface.

---

## ✨ Features

### 🔐 Authentication

* Password-protected login system
* Secure access to library operations

### 📖 Book Management

* View all books in the library
* Add new books to the inventory
* Update book availability
* Delete books from the database

### 👨‍🎓 Student Management

* View student records
* Track issued books
* Monitor due dates and pending dues

### 📚 Borrowing Operations

* Issue books to students
* Return borrowed books
* Automatically update book availability
* Maintain borrowing history records

### 💰 Dues Management

* Add dues to student accounts
* Clear outstanding dues
* Track pending payments

---

## 🛠️ Tech Stack

| Technology | Purpose                      |
| ---------- | ---------------------------- |
| Java       | Core Application Development |
| Java Swing | Graphical User Interface     |
| JDBC       | Database Connectivity        |
| MySQL      | Database Management System   |
| SQL        | Data Manipulation & Queries  |

---

## 🗄️ Database Schema

### `books_details`

Stores information related to books available in the library.

| Field     |
| --------- |
| book_id   |
| book_name |
| authour   |
| category  |
| available |
| issued    |

---

### `student_details`

Stores student information and issued book records.

| Field            |
| ---------------- |
| student_id       |
| student_name     |
| semester         |
| issued_book_id   |
| issued_book_name |
| due_date         |
| any_due          |

---

### `borrowed_books_details`

Stores information about currently borrowed books.

| Field       |
| ----------- |
| book_id     |
| book_name   |
| student_id  |
| borrowed_on |
| due_on      |

---

## ⚙️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/library-management-system.git
cd library-management-system
```

### 2. Create Database

```sql
CREATE DATABASE lib_manage_app;
```

### 3. Configure MySQL Connection

Update your database credentials inside the application:

```java
private static final String url = "jdbc:mysql://localhost:3306/lib_manage_app";
private static final String userName = "root";
private static final String password = "your_password";
```

### 4. Add MySQL JDBC Driver

Download and configure the MySQL Connector/J library in your project.

### 5. Run the Application

```bash
javac LibraryManagementGUI.java
java LibraryManagementGUI
```

---

## 🚀 Application Workflow

1. User logs into the system.
2. Librarian manages library inventory.
3. Books are issued to students.
4. Borrowing records are stored in the database.
5. Returned books automatically update inventory records.
6. Student dues can be managed and cleared.

---

## 📂 Project Structure

```text
LibraryManagementSystem/
│
├── LibraryManagementGUI.java
├── README.md
│
└── Database
    ├── books_details
    ├── student_details
    └── borrowed_books_details
```

---

## 🔮 Future Enhancements

* Role-Based Authentication
* Search & Filter Functionality
* Fine Calculation Automation
* Book Reservation System
* Email Notifications for Due Dates
* Report Generation (PDF/Excel)
* Enhanced User Interface
* Password Encryption
* Prepared Statements for Improved Security

---

## 👨‍💻 Author

**Deepesh Nehra**

B.Tech Computer Science Engineering (AI & ML)

Passionate about Software Development, Full Stack Development, Java Applications, and AI/ML.

---

## 📄 License

This project is intended for educational and learning purposes.
