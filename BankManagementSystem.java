import java.sql.*;
import java.util.Scanner;

public class BankManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to Database.");
            while (true) {
                System.out.println("1. Sign Up\n2. Login\n3. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1: signUp(); break;
                    case 2: login(); break;
                    case 3: System.exit(0);
                    default: System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void signUp() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        String query = "INSERT INTO users (username, password, balance) VALUES (?, ?, 0)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            System.out.println("Signup successful.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful.");
                userMenu(username);
            } else {
                System.out.println("Invalid credentials.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void userMenu(String username) {
        while (true) {
            System.out.println("1. Check Balance\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: checkBalance(username); break;
                case 2: deposit(username); break;
                case 3: withdraw(username); break;
                case 4: transfer(username); break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void checkBalance(String username) {
        String query = "SELECT balance FROM users WHERE username=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Balance: " + rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deposit(String username) {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        String query = "UPDATE users SET balance = balance + ? WHERE username=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setString(2, username);
            ps.executeUpdate();
            System.out.println("Deposit successful.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void withdraw(String username) {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        String query = "UPDATE users SET balance = balance - ? WHERE username=? AND balance >= ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setString(2, username);
            ps.setDouble(3, amount);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Withdrawal successful.");
            } else {
                System.out.println("Insufficient balance.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void transfer(String username) {
        System.out.print("Enter recipient username: ");
        String recipient = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();

        try {
            connection.setAutoCommit(false);
            String withdrawQuery = "UPDATE users SET balance = balance - ? WHERE username=? AND balance >= ?";
            try (PreparedStatement ps = connection.prepareStatement(withdrawQuery)) {
                ps.setDouble(1, amount);
                ps.setString(2, username);
                ps.setDouble(3, amount);
                if (ps.executeUpdate() == 0) {
                    System.out.println("Insufficient balance.");
                    connection.rollback();
                    return;
                }
            }
            String depositQuery = "UPDATE users SET balance = balance + ? WHERE username=?";
            try (PreparedStatement ps = connection.prepareStatement(depositQuery)) {
                ps.setDouble(1, amount);
                ps.setString(2, recipient);
                if (ps.executeUpdate() == 0) {
                    System.out.println("Recipient not found.");
                    connection.rollback();
                    return;
                }
            }
            connection.commit();
            System.out.println("Transfer successful.");
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            System.out.println("Error: " + e.getMessage());
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }
}
