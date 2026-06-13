import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class LibraryManagementGUI extends JFrame {

    private static final String url = "jdbc:mysql://localhost:3306/lib_manage_app";
    private static final String userName = "root";
    private static final String password = "622mysql";
    
    private Connection connection;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public LibraryManagementGUI() {
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Add panels
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createMainMenuPanel(), "MAIN");
        
        add(mainPanel);
        
        // Show login first
        cardLayout.show(mainPanel, "LOGIN");
        
        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // LOGIN PANEL
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(passLabel, gbc);
        
        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(passField, gbc);
        
        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);
        
        loginBtn.addActionListener(e -> {
            String pass = new String(passField.getPassword());
            if (pass.equals("123123")) {
                cardLayout.show(mainPanel, "MAIN");
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Password!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });
        
        return panel;
    }

    // MAIN MENU PANEL
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        
        String[] buttonLabels = {
            "View Books", "Insert Books", "Delete Books", "Update Books",
            "Issue Book", "Return Book", "Show Students", "Show Borrowed Books",
            "Pay Dues", "Add Dues", "Logout", ""
        };
        
        for (int i = 0; i < buttonLabels.length - 1; i++) {
            if (!buttonLabels[i].isEmpty()) {
                JButton btn = new JButton(buttonLabels[i]);
                btn.setFont(new Font("Arial", Font.PLAIN, 14));
                final int index = i;
                btn.addActionListener(e -> handleMenuAction(index));
                buttonPanel.add(btn);
            }
        }
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private void handleMenuAction(int choice) {
        switch(choice) {
            case 0: viewBooks(); break;
            case 1: insertBooks(); break;
            case 2: deleteBooks(); break;
            case 3: updateBooks(); break;
            case 4: issueBook(); break;
            case 5: returnBook(); break;
            case 6: showStudents(); break;
            case 7: showBorrowedBooks(); break;
            case 8: payDues(); break;
            case 9: addDues(); break;
            case 10: 
                cardLayout.show(mainPanel, "LOGIN");
                JOptionPane.showMessageDialog(this, "Logged out successfully!");
                break;
        }
    }

    // VIEW BOOKS
    private void viewBooks() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books_details");
            
            String[] columns = {"Book ID", "Book Name", "Author", "Category", "Available", "Issued"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("book_id"),
                    rs.getString("book_name"),
                    rs.getString("authour"),
                    rs.getString("category"),
                    rs.getInt("available"),
                    rs.getInt("issued")
                };
                model.addRow(row);
            }
            
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(700, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Books Details", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // INSERT BOOKS
    private void insertBooks() {
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField categoryField = new JTextField(15);
        JTextField availableField = new JTextField(5);
        JTextField issuedField = new JTextField(5);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Book ID:"));
        panel.add(idField);
        panel.add(new JLabel("Book Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Available:"));
        panel.add(availableField);
        panel.add(new JLabel("Issued:"));
        panel.add(issuedField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Insert Book", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                Statement stmt = connection.createStatement();
                String query = String.format(
                    "INSERT INTO books_details VALUES(%s,'%s','%s','%s',%s,%s)",
                    idField.getText(), nameField.getText(), authorField.getText(),
                    categoryField.getText(), availableField.getText(), issuedField.getText()
                );
                int inserted = stmt.executeUpdate(query);
                if (inserted > 0) {
                    JOptionPane.showMessageDialog(this, "Book inserted successfully!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // DELETE BOOKS
    private void deleteBooks() {
        String bookId = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");
        if (bookId != null && !bookId.isEmpty()) {
            try {
                Statement stmt = connection.createStatement();
                String query = String.format("DELETE FROM books_details WHERE book_id = %s", bookId);
                int deleted = stmt.executeUpdate(query);
                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // UPDATE BOOKS
    private void updateBooks() {
        JTextField idField = new JTextField(10);
        JTextField numberField = new JTextField(10);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Book ID:"));
        panel.add(idField);
        panel.add(new JLabel("New Available Count:"));
        panel.add(numberField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Update Book", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                Statement stmt = connection.createStatement();
                String query = String.format(
                    "UPDATE books_details SET available = %s WHERE book_id = %s",
                    numberField.getText(), idField.getText()
                );
                int updated = stmt.executeUpdate(query);
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Book updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ISSUE BOOK
    private void issueBook() {
        JTextField studentIdField = new JTextField(10);
        JTextField studentNameField = new JTextField(20);
        JTextField semesterField = new JTextField(5);
        JTextField bookIdField = new JTextField(10);
        JTextField bookNameField = new JTextField(20);
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        panel.add(new JLabel("Student Name:"));
        panel.add(studentNameField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterField);
        panel.add(new JLabel("Book ID:"));
        panel.add(bookIdField);
        panel.add(new JLabel("Book Name:"));
        panel.add(bookNameField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Issue Book", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                Statement stmt = connection.createStatement();
                
                stmt.executeUpdate(String.format(
                    "UPDATE books_details SET available=available-1 WHERE book_id = %s", 
                    bookIdField.getText()
                ));
                stmt.executeUpdate(String.format(
                    "UPDATE books_details SET issued=issued+1 WHERE book_id = %s", 
                    bookIdField.getText()
                ));
                stmt.executeUpdate(String.format(
                    "INSERT INTO borrowed_books_details VALUES(%s,'%s',%s,CURDATE(),DATE_ADD(CURDATE(), INTERVAL 15 DAY))",
                    bookIdField.getText(), bookNameField.getText(), studentIdField.getText()
                ));
                stmt.executeUpdate(String.format(
                    "INSERT INTO student_details VALUES(%s,'%s',%s,%s,'%s',DATE_ADD(CURDATE(), INTERVAL 15 DAY),0)",
                    studentIdField.getText(), studentNameField.getText(), semesterField.getText(),
                    bookIdField.getText(), bookNameField.getText()
                ));
                
                JOptionPane.showMessageDialog(this, "Book issued successfully!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // RETURN BOOK
    private void returnBook() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (studentId != null && !studentId.isEmpty()) {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT issued_book_id FROM student_details WHERE student_id = " + studentId
                );
                
                if (rs.next()) {
                    int bookId = rs.getInt("issued_book_id");
                    stmt.executeUpdate("DELETE FROM student_details WHERE student_id = " + studentId);
                    stmt.executeUpdate("UPDATE books_details SET available=available+1 WHERE book_id = " + bookId);
                    stmt.executeUpdate("UPDATE books_details SET issued=issued-1 WHERE book_id = " + bookId);
                    stmt.executeUpdate("DELETE FROM borrowed_books_details WHERE student_id = " + studentId);
                    
                    JOptionPane.showMessageDialog(this, "Book returned successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // SHOW STUDENTS
    private void showStudents() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student_details");
            
            String[] columns = {"Student ID", "Name", "Semester", "Book ID", "Book Name", "Due Date", "Dues"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getInt("semester"),
                    rs.getInt("issued_book_id"),
                    rs.getString("issued_book_name"),
                    rs.getDate("due_date"),
                    rs.getInt("any_due")
                };
                model.addRow(row);
            }
            
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(700, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Student Details", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SHOW BORROWED BOOKS
    private void showBorrowedBooks() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM borrowed_books_details");
            
            String[] columns = {"Book ID", "Book Name", "Student ID", "Borrowed On", "Due On"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("book_id"),
                    rs.getString("book_name"),
                    rs.getInt("student_id"),
                    rs.getDate("borrowed_on"),
                    rs.getDate("due_on")
                };
                model.addRow(row);
            }
            
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(650, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Borrowed Books", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // PAY DUES
    private void payDues() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID to pay dues:");
        if (studentId != null && !studentId.isEmpty()) {
            try {
                Statement stmt = connection.createStatement();
                String query = String.format("UPDATE student_details SET any_due = 0 WHERE student_id = %s", studentId);
                int updated = stmt.executeUpdate(query);
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Dues paid successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ADD DUES
    private void addDues() {
        JTextField idField = new JTextField(10);
        JTextField duesField = new JTextField(10);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Dues Amount:"));
        panel.add(duesField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Dues", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                Statement stmt = connection.createStatement();
                String query = String.format(
                    "UPDATE student_details SET any_due = %s WHERE student_id = %s",
                    duesField.getText(), idField.getText()
                );
                int updated = stmt.executeUpdate(query);
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Dues added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementGUI gui = new LibraryManagementGUI();
            gui.setVisible(true);
        });
    }
}
    

