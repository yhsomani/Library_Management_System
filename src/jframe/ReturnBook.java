package jframe;

import java.sql.*;
import javax.swing.JOptionPane;

public class ReturnBook extends javax.swing.JFrame {

    public ReturnBook() {
        initComponents();
    }
    // Return the Book 

    public boolean returnBook() {
        int bookID;
        int studentID;

        try {
            bookID = Integer.parseInt(inputBookId.getText().trim());
            studentID = Integer.parseInt(inputStudentId.getText().trim());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Book ID or Student ID format.");
            return false;
        }

        String sql = "UPDATE issue_book_details SET status = ? WHERE student_id = ? AND book_id = ? AND status = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "returned");
            pst.setInt(2, studentID);
            pst.setInt(3, bookID);
            pst.setString(4, "pending");

            int rowCount = pst.executeUpdate();
            return rowCount > 0;
        } catch (SQLException e) {
            showErrorDialog("Error while updating book status: " + e.getMessage());
            return false;
        }
    }

    // Fetch and Display Issued Book Details
    public void getIssueBookDetails() {
        int bookID;
        int studentID;

        try {
            bookID = Integer.parseInt(inputBookId.getText().trim());
            studentID = Integer.parseInt(inputStudentId.getText().trim());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Book ID or Student ID format.");
            return;
        }

        String sql = "SELECT * FROM issue_book_details WHERE book_id = ? AND student_id = ? AND status = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, bookID);
            pst.setInt(2, studentID);
            pst.setString(3, "pending");

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    outputIssueBookId.setText(rs.getString("id"));
                    outputBookName.setText(rs.getString("book_name"));
                    outputStudentName.setText(rs.getString("student_name"));
                    outputIssueDate.setText(rs.getString("issue_date"));
                    outputDueDate.setText(rs.getString("due_date"));
                } else {
                    showInfoDialog("No Record Found");
                    clearIssueBookDetails();
                }
            }
        } catch (SQLException e) {
            showErrorDialog("Error while fetching book details: " + e.getMessage());
        }
    }

    // Update Book Count
    public void updateBookCount() {
        int bookId;

        try {
            bookId = Integer.parseInt(inputBookId.getText().trim());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Book ID format.");
            return;
        }

        String sql = "UPDATE book_details SET quantity = quantity + 1 WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, bookId);
            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                showInfoDialog("Book Count Updated Successfully");
            } else {
                showErrorDialog("Book ID not found or count not updated.");
            }
        } catch (SQLException e) {
            showErrorDialog("Error while updating book count: " + e.getMessage());
        }
    }

    // Show Information Dialog
    private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Show Error Dialog
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Clear Issue Book Details
    private void clearIssueBookDetails() {
        outputIssueBookId.setText("");
        outputBookName.setText("");
        outputStudentName.setText("");
        outputIssueDate.setText("");
        outputDueDate.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        issueDate = new javax.swing.JLabel();
        issuedBookId = new javax.swing.JLabel();
        bookName = new javax.swing.JLabel();
        studentName = new javax.swing.JLabel();
        outputIssueBookId = new javax.swing.JLabel();
        outputBookName = new javax.swing.JLabel();
        outputStudentName = new javax.swing.JLabel();
        outputIssueDate = new javax.swing.JLabel();
        dueDate = new javax.swing.JLabel();
        outputDueDate = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        inputBookId = new app.bolivia.swing.JCTextField();
        inputStudentId = new app.bolivia.swing.JCTextField();
        manageBookUpdateBTN = new rojerusan.RSMaterialButtonRectangle();
        jLabel24 = new javax.swing.JLabel();
        manageBookUpdateBTN1 = new rojerusan.RSMaterialButtonRectangle();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
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

        issueDate.setBackground(new java.awt.Color(255, 255, 255));
        issueDate.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        issueDate.setForeground(new java.awt.Color(255, 255, 255));
        issueDate.setText("Issue Date:");
        jPanel7.add(issueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 260, -1, -1));

        issuedBookId.setBackground(new java.awt.Color(255, 255, 255));
        issuedBookId.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        issuedBookId.setForeground(new java.awt.Color(255, 255, 255));
        issuedBookId.setText("Issue ID:");
        jPanel7.add(issuedBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 60, -1, -1));

        bookName.setBackground(new java.awt.Color(255, 255, 255));
        bookName.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        bookName.setForeground(new java.awt.Color(255, 255, 255));
        bookName.setText("Book Name:");
        jPanel7.add(bookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 120, -1, -1));

        studentName.setBackground(new java.awt.Color(255, 255, 255));
        studentName.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        studentName.setForeground(new java.awt.Color(255, 255, 255));
        studentName.setText("Student Name:");
        jPanel7.add(studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 190, -1, -1));

        outputIssueBookId.setBackground(new java.awt.Color(255, 255, 255));
        outputIssueBookId.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputIssueBookId.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputIssueBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 60, -1, -1));

        outputBookName.setBackground(new java.awt.Color(255, 255, 255));
        outputBookName.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputBookName.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 120, -1, -1));

        outputStudentName.setBackground(new java.awt.Color(255, 255, 255));
        outputStudentName.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputStudentName.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputStudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 190, -1, -1));

        outputIssueDate.setBackground(new java.awt.Color(255, 255, 255));
        outputIssueDate.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputIssueDate.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputIssueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 260, -1, -1));

        dueDate.setBackground(new java.awt.Color(255, 255, 255));
        dueDate.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        dueDate.setForeground(new java.awt.Color(255, 255, 255));
        dueDate.setText("Due Date:");
        jPanel7.add(dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 330, -1, -1));

        outputDueDate.setBackground(new java.awt.Color(255, 255, 255));
        outputDueDate.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        outputDueDate.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.add(outputDueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 330, -1, -1));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 336, 430));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 0, 374, 650));

        jLabel4.setBackground(new java.awt.Color(255, 51, 51));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/Red_Books.png"))); // NOI18N
        jLabel4.setText(" Return Book");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 90, -1, -1));

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

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 150, -1, -1));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 51, 51));
        jLabel22.setText("Student ID:");
        jPanel9.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 150, -1, -1));

        inputBookId.setBackground(new java.awt.Color(255, 255, 255));
        inputBookId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
        inputBookId.setForeground(new java.awt.Color(255, 51, 51));
        inputBookId.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        inputBookId.setPhColor(new java.awt.Color(255, 51, 51));
        inputBookId.setPlaceholder("Book ID");
        inputBookId.setSelectionColor(new java.awt.Color(255, 51, 51));
        jPanel9.add(inputBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 55, 240, -1));

        inputStudentId.setBackground(new java.awt.Color(255, 255, 255));
        inputStudentId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
        inputStudentId.setForeground(new java.awt.Color(255, 51, 51));
        inputStudentId.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        inputStudentId.setPhColor(new java.awt.Color(255, 51, 51));
        inputStudentId.setPlaceholder("Student ID");
        inputStudentId.setSelectionColor(new java.awt.Color(255, 51, 51));
        jPanel9.add(inputStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 145, 240, -1));

        manageBookUpdateBTN.setBackground(new java.awt.Color(102, 102, 255));
        manageBookUpdateBTN.setText("Find Data");
        manageBookUpdateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageBookUpdateBTNActionPerformed(evt);
            }
        });
        jPanel9.add(manageBookUpdateBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 250, 50));

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 51, 51));
        jLabel24.setText("Book ID:");
        jPanel9.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 60, -1, -1));

        manageBookUpdateBTN1.setBackground(new java.awt.Color(255, 51, 51));
        manageBookUpdateBTN1.setText("Return Book");
        manageBookUpdateBTN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageBookUpdateBTN1ActionPerformed(evt);
            }
        });
        jPanel9.add(manageBookUpdateBTN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 250, 50));

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 150, 380, 410));

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

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1320, 0, 90, 35));

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

        jPanel1.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/library-2.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, 40, 660, 610));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1410, 650));

        setSize(new java.awt.Dimension(1407, 650));
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

    private void manageBookUpdateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageBookUpdateBTNActionPerformed
        getIssueBookDetails();
    }//GEN-LAST:event_manageBookUpdateBTNActionPerformed

    private void manageBookUpdateBTN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageBookUpdateBTN1ActionPerformed
        if (returnBook()) {
            showInfoDialog("Book Returned Successfully");
            updateBookCount();
            clearIssueBookDetails();
        } else {
            showErrorDialog("Book Return Failed");
        }
    }//GEN-LAST:event_manageBookUpdateBTN1ActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReturnBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bookName;
    private javax.swing.JLabel dueDate;
    private app.bolivia.swing.JCTextField inputBookId;
    private app.bolivia.swing.JCTextField inputStudentId;
    private javax.swing.JLabel issueDate;
    private javax.swing.JLabel issuedBookId;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private rojerusan.RSMaterialButtonRectangle manageBookUpdateBTN;
    private rojerusan.RSMaterialButtonRectangle manageBookUpdateBTN1;
    private javax.swing.JLabel outputBookName;
    private javax.swing.JLabel outputDueDate;
    private javax.swing.JLabel outputIssueBookId;
    private javax.swing.JLabel outputIssueDate;
    private javax.swing.JLabel outputStudentName;
    private javax.swing.JLabel studentName;
    // End of variables declaration//GEN-END:variables
}
