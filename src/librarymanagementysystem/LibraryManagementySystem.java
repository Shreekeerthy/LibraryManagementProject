package librarymanagementysystem;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.JTableHeader;

public class LibraryManagementySystem {
    private static final String url = "jdbc:mysql://localhost:3306/lib1";
    private static final String username = "root";
    private static final String password = "KKSK12";

    private static Connection con;
    private static Statement st;
    private static ResultSet rs;

    public static void main(String[] args) throws SQLException {
        connect();
        SwingUtilities.invokeLater(() -> {
            loginGUI();
        });
    }

    private static void connect() {
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to Database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loginGUI() {
    JFrame frame = new JFrame("LIBRARY MANAGEMENT SYSTEM");
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    // Create labels and text fields
    JLabel userLabel = new JLabel("ENTER USERNAME:");
    JLabel passLabel = new JLabel("ENTER PASSWORD:");
    JTextField userField = new JTextField(15);
    JPasswordField passField = new JPasswordField(15);
    JButton loginButton = new JButton("LOGIN");

    // Set GridBagConstraints properties for centering
    gbc.insets = new Insets(10, 10, 10, 10); // Space around components
    gbc.anchor = GridBagConstraints.CENTER; // Center components
    gbc.fill = GridBagConstraints.NONE; // Do not resize the components

    // Add components to the panel
    gbc.gridx = 0; gbc.gridy = 0; panel.add(userLabel, gbc);
    gbc.gridx = 1; gbc.gridy = 0; panel.add(userField, gbc);
    gbc.gridx = 0; gbc.gridy = 1; panel.add(passLabel, gbc);
    gbc.gridx = 1; gbc.gridy = 1; panel.add(passField, gbc);
    gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; // Span across columns
    panel.add(loginButton, gbc);

    // Set the frame properties
    frame.getContentPane().add(panel);
    frame.setSize(300, 200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null); // Center the window on the screen
    frame.setVisible(true);

    // Login Button Action Listener
    loginButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (login(username, password)) {
                if (isAdmin()) {
                    frame.dispose();
                    adminGUI();
                } else {
                    frame.dispose();
                    userGUI();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Login Failed..Try Again..", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
}

    private static boolean login(String user, String pswd) {
        try {
            PreparedStatement ps1 = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps1.setString(1, user);
            ps1.setString(2, pswd);
            rs = ps1.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isAdmin() {
        try {
            return rs.getString("role").equalsIgnoreCase("admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // GUI for Admin Menu
    // GUI for Admin Menu
private static void adminGUI() {
    JFrame frame = new JFrame("Admin Menu");
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.insets = new Insets(10, 10, 10, 10); // Padding
    gbc.anchor = GridBagConstraints.CENTER; // Center the components
    gbc.fill = GridBagConstraints.NONE; // No fill for centered components

    JButton displayBooksButton = new JButton("Display Books");
    gbc.gridx = 0; gbc.gridy = 0; panel.add(displayBooksButton, gbc);

    JButton addBookButton = new JButton("Add Book");
    gbc.gridy++; panel.add(addBookButton, gbc);

    JButton updateBookButton = new JButton("Update Book");
    gbc.gridy++; panel.add(updateBookButton, gbc);

    JButton borrowBookButton = new JButton("Borrow Book");
    gbc.gridy++; panel.add(borrowBookButton, gbc);

    JButton returnBookButton = new JButton("Return Book");
    gbc.gridy++; panel.add(returnBookButton, gbc);

    JButton viewTableButton = new JButton("View All Books");
    gbc.gridy++; panel.add(viewTableButton, gbc);  // New button for viewing all books

    JButton disconnectButton = new JButton("Disconnect");
    gbc.gridy++; panel.add(disconnectButton, gbc);

    frame.getContentPane().add(panel);
    frame.setSize(400, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    displayBooksButton.addActionListener(e -> displayBooks());
    addBookButton.addActionListener(e -> addBookGUI());
    updateBookButton.addActionListener(e -> updateBookGUI());
    borrowBookButton.addActionListener(e -> borrowBookGUI());
    returnBookButton.addActionListener(e -> returnBookGUI());

    // Add action listener for the new button
    viewTableButton.addActionListener(e -> viewAllBooks());

    disconnectButton.addActionListener(e -> {
        disconnect();
        frame.dispose();
    });
}


    // GUI for User Menu
    private static void userGUI() {
        JFrame frame = new JFrame("User Menu");
        JPanel panel = new JPanel();
        JButton displayBooksButton = new JButton("Display Books");
        JButton borrowBookButton = new JButton("Borrow Book");
        JButton returnBookButton = new JButton("Return Book");
        JButton exitButton = new JButton("Exit");

        panel.add(displayBooksButton);
        panel.add(borrowBookButton);
        panel.add(returnBookButton);
        panel.add(exitButton);

        frame.getContentPane().add(panel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        displayBooksButton.addActionListener(e -> displayBooks());
        borrowBookButton.addActionListener(e -> borrowBookGUI());
        returnBookButton.addActionListener(e -> returnBookGUI());
        exitButton.addActionListener(e -> frame.dispose());
    }

    // Display Books
    private static void displayBooks() {
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM books");
            StringBuilder bookList = new StringBuilder();
            while (rs.next()) {
                bookList.append("ID: ").append(rs.getInt("id"))
                        .append(", Title: ").append(rs.getString("title"))
                        .append(", Author: ").append(rs.getString("author"))
                        .append(", Available: ").append(rs.getBoolean("available") ? "YES" : "NO").append("\n");
            }
            JOptionPane.showMessageDialog(null, bookList.toString(), "Books Available", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all books
private static void viewAllBooks() {
    try {
        // Query to get all books
        PreparedStatement ps1 = con.prepareStatement("SELECT * FROM books");
        rs = ps1.executeQuery();

        // Get metadata to create column names
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Column names
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = metaData.getColumnName(i + 1);
        }

        // Data for the table
        ArrayList<Object[]> rowData = new ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            rowData.add(row);
        }

        // Convert the row data to an array
        Object[][] data = new Object[rowData.size()][columnCount];
        for (int i = 0; i < rowData.size(); i++) {
            data[i] = rowData.get(i);
        }

        // Create the JTable with a non-editable model
        JTable table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent cell editing
            }
        };

        // Set preferred size and auto resize
        table.setPreferredScrollableViewportSize(new Dimension(500, 200)); // Set preferred size
        table.setFillsViewportHeight(true); // Make the table fill the scroll pane
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Adjust column width to fit content
        table.setRowHeight(30); // Set row height

        // Set font for the table data
        Font dataFont = new Font("Arial", Font.PLAIN, 14); // Change font and size as needed
        table.setFont(dataFont);

        // Set font for the table headers
        Font headerFont = new Font("Arial", Font.BOLD, 16); // Bold font for headers
        JTableHeader header = table.getTableHeader();
        header.setFont(headerFont);

        // Add thick borders to the table
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // Border for the table with thickness 3
        table.setShowGrid(true); // Show grid lines
        table.setGridColor(Color.GRAY); // Set grid color

        // Wrap JTable in JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 3), "Books")); // Thicker border for the scroll pane

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Center the table
        gbc.insets = new Insets(10, 10, 10, 10); // Set insets for spacing
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH; // Allow the scroll pane to grow
        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(scrollPane, gbc); // Add scroll pane with table to panel

        // Create a new frame to show the table
        JFrame frame = new JFrame("All Books");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.setSize(600, 300); // Set size for the frame
        frame.setVisible(true);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error fetching data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    // GUI for Adding Books
    private static void addBookGUI() {
        JFrame frame = new JFrame("Add Book");
        JPanel panel = new JPanel();
        JLabel titleLabel = new JLabel("Title:");
        JLabel authorLabel = new JLabel("Author:");
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JButton addButton = new JButton("Add");

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(addButton);

        frame.getContentPane().add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            addBook(title, author);
            frame.dispose();
        });
    }

    private static void addBook(String title, String author) {
        try {
            PreparedStatement ps1 = con.prepareStatement("INSERT INTO books(title, author, available) VALUES (?, ?, true)");
            ps1.setString(1, title);
            ps1.setString(2, author);
            int rows = ps1.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Book added successfully...! Hooray🥳");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add book.. Sorry😔");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GUI for Updating Book
    private static void updateBookGUI() {
        JFrame frame = new JFrame("Update Book");
        JPanel panel = new JPanel();
        JLabel idLabel = new JLabel("Book ID:");
        JLabel titleLabel = new JLabel("New Title:");
        JLabel authorLabel = new JLabel("New Author:");
        JTextField idField = new JTextField(15);
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JButton updateButton = new JButton("Update");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(updateButton);

        frame.getContentPane().add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        updateButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String author = authorField.getText();
            updateBook(id, title, author);
            frame.dispose();
        });
    }

    private static void updateBook(int id, String title, String author) {
        try {
            PreparedStatement ps1 = con.prepareStatement("UPDATE books SET title=?, author=? WHERE id=?");
            ps1.setString(1, title);
            ps1.setString(2, author);
            ps1.setInt(3, id);
            int rows = ps1.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Book updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GUI for Borrowing Book
    private static void borrowBookGUI() {
        JFrame frame = new JFrame("Borrow Book");
        JPanel panel = new JPanel();
        JLabel idLabel = new JLabel("Book ID:");
        JTextField idField = new JTextField(15);
        JButton borrowButton = new JButton("Borrow");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(borrowButton);

        frame.getContentPane().add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        borrowButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            borrowBook(id);
            frame.dispose();
        });
    }

    private static void borrowBook(int id) {
        try {
            PreparedStatement ps1 = con.prepareStatement("UPDATE books SET available=false WHERE id=?");
            ps1.setInt(1, id);
            int rows = ps1.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Book borrowed successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Book is not available or does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GUI for Returning Book
    private static void returnBookGUI() {
        JFrame frame = new JFrame("Return Book");
        JPanel panel = new JPanel();
        JLabel idLabel = new JLabel("Book ID:");
        JTextField idField = new JTextField(15);
        JButton returnButton = new JButton("Return");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(returnButton);

        frame.getContentPane().add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        returnButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            returnBook(id);
            frame.dispose();
        });
    }

    private static void returnBook(int id) {
        try {
            PreparedStatement ps1 = con.prepareStatement("UPDATE books SET available=true WHERE id=?");
            ps1.setInt(1, id);
            int rows = ps1.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Book returned successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Book does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void disconnect() {
        try {
            if (con != null) {
                con.close();
                System.out.println("Disconnected from Database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
