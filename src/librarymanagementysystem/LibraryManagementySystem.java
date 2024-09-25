package librarymanagementysystem;

import java.sql.*;
import java.util.Scanner;

public class LibraryManagementySystem {
    private static final String url = "jdbc:mysql://localhost:3306/library";
    private static final String username = "root";
    private static final String password = "Keerthy1112@";

    private static Connection con;
    private static Statement st;
    private static ResultSet rs;

    public static void main(String[] args) throws SQLException {
        connect();
        if (login()) {
            if (isAdmin()) {
                System.out.println("Admin Login Successful!");
                adminMenu();
            } else {
                System.out.println("User Login Successful!");
                userMenu();
            }
        }
        disconnect();
    }

    private static void connect() {
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to Database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean login() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pswd = sc.nextLine();

        try {
            PreparedStatement ps1 = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps1.setString(1, user);
            ps1.setString(2, pswd);
            rs = ps1.executeQuery();
            if (rs.next()) {
                System.out.println("Login Successful......");
                return true;
            } else {
                System.out.println("Invalid username or password");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    }

    private static boolean isAdmin() throws SQLException {
        if (rs.getString("role").equalsIgnoreCase("admin")) {
            return true;
        } else {
            return false;
        }
    }

    private static void adminMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Admin Menu:");
            System.out.println("1. Display Books");
            System.out.println("2. Add Book");
            System.out.println("3. Update Book");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("0. Disconnect");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    displayBooks();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    borrowBook();
                    break;
                case 5:
                    returnBook();
                    break;
                case 0:
                    disconnect();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private static void userMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("User Menu:");
            System.out.println("1. Display Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    displayBooks();
                    break;
                case 2:
                    borrowBook();
                    break;
                case 3:
                    returnBook();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

   private static void displayBooks() {
    try {
        st = con.createStatement();
        rs = st.executeQuery("SELECT * FROM books");
        
        System.out.println("+----+----------------------+----------------------+-----------+");
        System.out.println("| ID |        Title         |        Author        | Available |");
        System.out.println("+----+----------------------+----------------------+-----------+");
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            boolean available = rs.getBoolean("available");

            System.out.printf("| %-3d| %-20s| %-20s| %-10s|\n", id, title, author, available ? "Yes" : "No");
        }
        
        System.out.println("+----+----------------------+----------------------+-----------+");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    private static void addBook() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter title of book");
            String title = sc.nextLine();
            System.out.println("Enter author of book");
            String author = sc.nextLine();
            PreparedStatement ps1 = con.prepareStatement("INSERT INTO books(title, author, available) VALUES (?, ?, true)");
            ps1.setString(1, title);
            ps1.setString(2, author);
            int rows = ps1.executeUpdate();
            if (rows > 0)
                System.out.println("Book Added Successfully");
            else
                System.out.println("Failed to add book");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateBook() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter ID of Book");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter new title of book");
            String title = sc.nextLine();
            System.out.println("Enter new author of book");
            String author = sc.nextLine();
            PreparedStatement ps1 = con.prepareStatement("UPDATE books SET title=?, author=? WHERE id=?");
            ps1.setString(1, title);
            ps1.setString(2, author);
            ps1.setInt(3, id);
            int rows = ps1.executeUpdate();
            if (rows > 0)
                System.out.println("Book Updated Successfully");
            else
                System.out.println("Failed to update book");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void borrowBook() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter id of book to borrow:");
            int id = sc.nextInt();
            PreparedStatement ps1 = con.prepareStatement("UPDATE books SET available=false WHERE id=? AND available=true");
            ps1.setInt(1, id);
            int rows = ps1.executeUpdate();
            if (rows > 0)
                System.out.println("Book borrowed Successfully");
            else
                System.out.println("Failed to Borrow");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void returnBook() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter id of book to return:");
            int id = sc.nextInt();
            PreparedStatement ps1 = con.prepareStatement("UPDATE books SET available=true WHERE id=? AND available=false");
            ps1.setInt(1, id);
            int rows = ps1.executeUpdate();
            if (rows > 0)
                System.out.println("Book Returned Successfully");
            else
                System.out.println("Failed to Return");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void disconnect() {
        try {
            if (rs != null)
                rs.close();
            if (st != null)
                st.close();
            if (con != null)
                con.close();
            System.out.println("Disconnected from Database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
