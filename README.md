# Bank Management System

A **fully functional Bank Management System** built using **Java** with **MySQL database integration**. This system allows users to **sign up, log in, deposit, withdraw, transfer money, and check their balance** with proper validation and exception handling.

## Description
The **Bank Management System** is a console-based Java application that simulates a banking environment. It enables users to create accounts, securely log in, manage their funds, and perform transactions with ease. The application interacts with a MySQL database to store user information and transaction history, ensuring data persistence and reliability. Designed with security in mind, the system incorporates proper exception handling and validation to prevent invalid operations.

## Features
✅ User Authentication (Signup/Login with validation)
✅ Deposit & Withdraw Money
✅ Transfer Money to Other Users
✅ Check Account Balance
✅ Transaction History (Stored in MySQL)
✅ MySQL Database Integration using JDBC
✅ Exception Handling & Secure Transactions

## Technologies Used
- **Java** (Core Java, JDBC)
- **MySQL** (Database)
- **Maven** (Dependency Management)

## Installation & Setup
### 1️⃣ Clone the Repository
```sh
$ git clone https://github.com/your-username/bank-management-system.git
$ cd bank-management-system
```

### 2️⃣ Configure MySQL Database
1. Open MySQL and create a database:
```sql
CREATE DATABASE bank;
USE bank;
```
2. Create the `users` table:
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DOUBLE DEFAULT 0
);
```
3. Create the `transactions` table:
```sql
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(50) NOT NULL,
    recipient VARCHAR(50),
    amount DOUBLE NOT NULL,
    transaction_type ENUM('Deposit', 'Withdraw', 'Transfer') NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender) REFERENCES users(username),
    FOREIGN KEY (recipient) REFERENCES users(username)
);
```

### 3️⃣ Install Dependencies (If using Maven)
```sh
$ mvn clean install
```

### 4️⃣ Run the Application
Compile and run the Java application:
```sh
$ javac BankManagementSystem.java
$ java BankManagementSystem
```

## Usage
1. **Sign Up** - Create a new account
2. **Log In** - Authenticate with username and password
3. **Deposit** - Add money to your account
4. **Withdraw** - Withdraw money (if balance allows)
5. **Transfer** - Send money to another user
6. **Check Balance** - View available balance
7. **Exit** - Quit the system

## Troubleshooting
- **Error: No suitable driver found** → Ensure MySQL Connector is added.
- **MySQL Connection Error** → Check if MySQL is running and credentials are correct.
- **Compilation Issues** → Run `mvn clean install` to resolve dependencies.

## License
This project is **open-source** and available under the MIT License.

## Contributing
Feel free to fork, submit issues, or contribute improvements!

### 📌 Author: Sujit Kumar Mahato
🚀 GitHub: https://github.com/Sujitraj07

