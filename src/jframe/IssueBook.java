package jframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class IssueBook extends javax.swing.JFrame {

    public IssueBook() {
        initComponents();
    }
// Check whether the book is already issued or not

    public boolean isBookAlreadyIssued() {
        int bookId;
        int studentId;

        try {
            bookId = Integer.parseInt(inputBookId.getText().trim());
            studentId = Integer.parseInt(inputStudentId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Book ID or Student ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "SELECT * FROM issue_book_details WHERE book_id = ? AND student_id = ? AND status = 'pending'";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, bookId);
            pst.setInt(2, studentId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {  // If a result is found, the book is already issued
                    return true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error while checking book issuance.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return false;
    }

// Update Book Count
    public void updateBookCount() {
        int bookId;

        try {
            bookId = Integer.parseInt(inputBookId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Book ID format", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE book_details SET quantity = quantity - 1 WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, bookId);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Book Count Updated");

                int initialCount = Integer.parseInt(outputQuantity.getText().trim());
                outputQuantity.setText(Integer.toString(initialCount - 1));
            } else {
                JOptionPane.showMessageDialog(this, "Book ID not found or count not updated", "Update Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error while updating book count", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

// Insert issued book details into database
    public boolean issueBook() {
        int bookId;
        int studentId;

        try {
            bookId = Integer.parseInt(inputBookId.getText().trim());
            studentId = Integer.parseInt(inputStudentId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Book ID or Student ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String bookName = outputBookName.getText().trim();
        String studentName = outputStudentName.getText().trim();

        if (bookName.isEmpty() || studentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book name and student name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        java.util.Date uIssuedDate = issueDate.getDatoFecha();
        java.util.Date uDueDate = dueDate.getDatoFecha();
        if (uIssuedDate == null || uDueDate == null) {
            JOptionPane.showMessageDialog(this, "Issue date and due date cannot be null.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        java.sql.Date sIssuedDate = new java.sql.Date(uIssuedDate.getTime());
        java.sql.Date sDueDate = new java.sql.Date(uDueDate.getTime());

        String sql = "INSERT INTO issue_book_details (book_id, book_name, student_id, student_name, issue_date, due_date, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'pending')";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, bookId);
            pst.setString(2, bookName);
            pst.setInt(3, studentId);
            pst.setString(4, studentName);
            pst.setDate(5, sIssuedDate);
            pst.setDate(6, sDueDate);

            int rowCount = pst.executeUpdate();
            return rowCount > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error occurred while issuing the book.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

// Get student details
    public void getStudentDetails() {
        String input = inputStudentId.getText().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Student ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int studentId;
        try {
            studentId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID format. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT student_id, student_name, course, branch FROM student_details WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, studentId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    outputStudentId.setText(rs.getString("student_id"));
                    outputStudentName.setText(rs.getString("student_name"));
                    outputCourse.setText(rs.getString("course"));
                    outputBranch.setText(rs.getString("branch"));
                } else {
                    JOptionPane.showMessageDialog(this, "No student found with the provided ID.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                    clearStudentDetails();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "An error occurred while retrieving student details.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

// Clear student details fields
    private void clearStudentDetails() {
        outputStudentId.setText("");
        outputStudentName.setText("");
        outputCourse.setText("");
        outputBranch.setText("");
    }

// Get book details
    public void getBookDetails() {
        String input = inputBookId.getText().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Book ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Book ID format. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT book_id, book_name, author, quantity FROM book_details WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, bookId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    outputBookId.setText(rs.getString("book_id"));
                    outputBookName.setText(rs.getString("book_name"));
                    outputAuthor.setText(rs.getString("author"));
                    outputQuantity.setText(rs.getString("quantity"));
                } else {
                    JOptionPane.showMessageDialog(this, "No book found with the provided ID.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                    clearBookDetails();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "An error occurred while retrieving book details.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

// Clear book details fields
    private void clearBookDetails() {
        outputBookId.setText("");
        outputBookName.setText("");
        outputAuthor.setText("");
        outputQuantity.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        outputBookId = new javax.swing.JLabel();
        outputBookName = new javax.swing.JLabel();
        outputAuthor = new javax.swing.JLabel();
        outputQuantity = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        outputStudentId = new javax.swing.JLabel();
        outputStudentName = new javax.swing.JLabel();
        outputCourse = new javax.swing.JLabel();
        outputBranch = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        inputBookId = new app.bolivia.swing.JCTextField();
        inputStudentId = new app.bolivia.swing.JCTextField();
        dueDate = new rojeru_san.componentes.RSDateChooser();
        issueDate = new rojeru_san.componentes.RSDateChooser();
        manageBookUpdateBTN = new rojerusan.RSMaterialButtonRectangle();
        jLabel24 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(375, 550));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(375, 550));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));
        jPanel2.setPreferredSize(new java.awt.Dimension(375, 550));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/White_Literature.png"))); // NOI18N
        jLabel3.setText(" Book Details");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 92, -1, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 2));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 150, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 51, 51));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Quantity:");
        jPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 260, -1, -1));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Book ID:");
        jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 60, -1, -1));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Name:");
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 120, -1, -1));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Author:");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 190, -1, -1));

        outputBookId.setBackground(new java.awt.Color(255, 255, 255));
        outputBookId.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputBookId.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, -1, -1));

        outputBookName.setBackground(new java.awt.Color(255, 255, 255));
        outputBookName.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputBookName.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, -1, -1));

        outputAuthor.setBackground(new java.awt.Color(255, 255, 255));
        outputAuthor.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputAuthor.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputAuthor, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, -1, -1));

        outputQuantity.setBackground(new java.awt.Color(255, 255, 255));
        outputQuantity.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputQuantity.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, -1, -1));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 336, 300));

        jPanel10.setBackground(new java.awt.Color(102, 102, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 17)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/White_Rewind.png"))); // NOI18N
        jLabel8.setText("Back");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel10.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 100, 35));

        jPanel2.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 35));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 374, 650));

        jLabel4.setBackground(new java.awt.Color(255, 51, 51));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/Red_Books.png"))); // NOI18N
        jLabel4.setText(" Issue Book");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 92, -1, -1));

        jPanel6.setBackground(new java.awt.Color(255, 51, 51));
        jPanel6.setPreferredSize(new java.awt.Dimension(300, 2));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 150, -1, -1));

        jPanel3.setBackground(new java.awt.Color(102, 102, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(375, 550));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/White_Student_Registration.png"))); // NOI18N
        jLabel2.setText(" Students Details");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 92, -1, -1));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(300, 2));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 150, -1, -1));

        jPanel8.setBackground(new java.awt.Color(102, 102, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Branch:");
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 260, -1, -1));

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Student ID:");
        jPanel8.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 60, -1, -1));

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Name:");
        jPanel8.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 120, -1, -1));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Course:");
        jPanel8.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 190, -1, -1));

        outputStudentId.setBackground(new java.awt.Color(255, 255, 255));
        outputStudentId.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputStudentId.setForeground(new java.awt.Color(255, 255, 255));
        jPanel8.add(outputStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, -1, -1));

        outputStudentName.setBackground(new java.awt.Color(255, 255, 255));
        outputStudentName.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputStudentName.setForeground(new java.awt.Color(255, 255, 255));
        jPanel8.add(outputStudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, -1, -1));

        outputCourse.setBackground(new java.awt.Color(255, 255, 255));
        outputCourse.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputCourse.setForeground(new java.awt.Color(255, 255, 255));
        jPanel8.add(outputCourse, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, -1, -1));

        outputBranch.setBackground(new java.awt.Color(255, 255, 255));
        outputBranch.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputBranch.setForeground(new java.awt.Color(255, 255, 255));
        jPanel8.add(outputBranch, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, -1, -1));

        jPanel3.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 336, 300));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 374, 650));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 51, 51));
        jLabel20.setText("Due Date:");
        jPanel9.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 260, -1, -1));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 51, 51));
        jLabel22.setText("Student ID:");
        jPanel9.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 120, -1, -1));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 51, 51));
        jLabel23.setText("Issue Date:");
        jPanel9.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 190, -1, -1));

        inputBookId.setBackground(new java.awt.Color(255, 255, 255));
        inputBookId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
        inputBookId.setForeground(new java.awt.Color(255, 51, 51));
        inputBookId.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        inputBookId.setPhColor(new java.awt.Color(255, 51, 51));
        inputBookId.setPlaceholder("Enter Book ID");
        inputBookId.setSelectionColor(new java.awt.Color(255, 51, 51));
        inputBookId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputBookIdFocusLost(evt);
            }
        });
        jPanel9.add(inputBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 55, 240, -1));

        inputStudentId.setBackground(new java.awt.Color(255, 255, 255));
        inputStudentId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
        inputStudentId.setForeground(new java.awt.Color(255, 51, 51));
        inputStudentId.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        inputStudentId.setPhColor(new java.awt.Color(255, 51, 51));
        inputStudentId.setPlaceholder("Enter Student ID");
        inputStudentId.setSelectionColor(new java.awt.Color(255, 51, 51));
        inputStudentId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputStudentIdFocusLost(evt);
            }
        });
        jPanel9.add(inputStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 115, 240, -1));

        dueDate.setColorBackground(new java.awt.Color(255, 51, 51));
        dueDate.setColorForeground(new java.awt.Color(255, 51, 51));
        dueDate.setFocusable(false);
        dueDate.setPlaceholder("Select Due Date");
        jPanel9.add(dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, -1, -1));

        issueDate.setColorBackground(new java.awt.Color(255, 51, 51));
        issueDate.setColorForeground(new java.awt.Color(255, 51, 51));
        issueDate.setFocusable(false);
        issueDate.setPlaceholder("Select Issue Date");
        jPanel9.add(issueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, -1, -1));

        manageBookUpdateBTN.setBackground(new java.awt.Color(255, 51, 51));
        manageBookUpdateBTN.setText("Update");
        manageBookUpdateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageBookUpdateBTNActionPerformed(evt);
            }
        });
        jPanel9.add(manageBookUpdateBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 250, 50));

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 51, 51));
        jLabel24.setText("Book ID:");
        jPanel9.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 60, -1, -1));

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 160, 380, 410));

        jPanel11.setBackground(new java.awt.Color(255, 51, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText(" X");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });
        jPanel11.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 35, 35));

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 0, 90, 35));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 650));

        setSize(new java.awt.Dimension(1200, 650));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

// Mouse click event to navigate to the homepage
    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        this.dispose();  // Close the current window
    }//GEN-LAST:event_jLabel8MouseClicked

// Mouse click event to handle exit confirmation
    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);  // Exit the application
        }
    }//GEN-LAST:event_jLabel9MouseClicked

    // Manage Book Update Button Action Performed
    private void manageBookUpdateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageBookUpdateBTNActionPerformed
        try {
            // Check if the book quantity is greater than 0
            if (!outputQuantity.getText().equals("0")) {
                // Check if the book is already issued to the student
                if (!isBookAlreadyIssued()) {
                    // Try to issue the book
                    if (issueBook()) {
                        // Show success message if the book is issued
                        JOptionPane.showMessageDialog(this, "Book issued successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Update the book count after successfully issuing the book
                        updateBookCount();
                    } else {
                        // Show failure message if the book couldn't be issued
                        JOptionPane.showMessageDialog(this, "Unable to issue the book. Please check the input details and try again.", "Failure", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Show a message if the book is already issued to the student
                    JOptionPane.showMessageDialog(this, "This book is already issued to the student.", "Information", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                // Show a message if the book quantity is 0, indicating that the book is unavailable
                JOptionPane.showMessageDialog(this, "This book is currently unavailable (quantity is 0).", "Unavailable", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            // Show an error message if something goes wrong
            JOptionPane.showMessageDialog(this, "An error occurred while issuing the book. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();  // Log the exception for debugging purposes
        }
    }//GEN-LAST:event_manageBookUpdateBTNActionPerformed

// Handle focus lost event for Book ID input field
    private void inputBookIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputBookIdFocusLost
        String bookIdText = inputBookId.getText().trim();
        if (!bookIdText.isEmpty()) {
            try {
                Integer.parseInt(bookIdText);  // Validate if it is a numeric value
                getBookDetails();  // Fetch and display book details
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Book ID format. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid Book ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_inputBookIdFocusLost

// Handle focus lost event for Student ID input field
    private void inputStudentIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputStudentIdFocusLost
        String studentIdText = inputStudentId.getText().trim();
        if (!studentIdText.isEmpty()) {
            try {
                Integer.parseInt(studentIdText);  // Validate if it is a numeric value
                getStudentDetails();  // Fetch and display student details
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Student ID format. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid Student ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_inputStudentIdFocusLost

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IssueBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.componentes.RSDateChooser dueDate;
    private app.bolivia.swing.JCTextField inputBookId;
    private app.bolivia.swing.JCTextField inputStudentId;
    private rojeru_san.componentes.RSDateChooser issueDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private rojerusan.RSMaterialButtonRectangle manageBookUpdateBTN;
    private javax.swing.JLabel outputAuthor;
    private javax.swing.JLabel outputBookId;
    private javax.swing.JLabel outputBookName;
    private javax.swing.JLabel outputBranch;
    private javax.swing.JLabel outputCourse;
    private javax.swing.JLabel outputQuantity;
    private javax.swing.JLabel outputStudentId;
    private javax.swing.JLabel outputStudentName;
    // End of variables declaration//GEN-END:variables
}
