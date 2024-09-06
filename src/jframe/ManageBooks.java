
package jframe;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author ysoma
 */
public class ManageBooks extends javax.swing.JFrame {


    private DefaultTableModel model;

    public ManageBooks() {
        initComponents();
        setBookDetailsToTable();
    }

    private boolean isBookIdExists(int bookId) {
        String sqlQuery = "SELECT COUNT(*) FROM book_details WHERE book_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sqlQuery)) {
            pst.setInt(1, bookId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            showError("Error checking book ID existence", e);
        }
        return false;
    }

    private void setBookDetailsToTable() {
        String sqlQuery = "SELECT * FROM book_details";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sqlQuery)) {

            model = (DefaultTableModel) tbl_bookDetails.getModel();
            model.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("book_id"),
                    rs.getString("book_name"),
                    rs.getString("author"),
                    rs.getInt("quantity")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            showError("Error fetching book details", e);
        }
    }

    private boolean addBook() {
        String sqlQuery = "INSERT INTO book_details (book_id, book_name, author, quantity) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sqlQuery)) {

            pst.setInt(1, Integer.parseInt(txt_BookID.getText().trim()));
            pst.setString(2, txt_Book_Name.getText().trim());
            pst.setString(3, txt_Author.getText().trim());
            pst.setInt(4, Integer.parseInt(txt_Quantity.getText().trim()));

            return pst.executeUpdate() > 0;
        } catch (NumberFormatException e) {
            showError("Book ID and Quantity must be integers", e);
        } catch (SQLException e) {
            showError("Error adding book", e);
        }
        return false;
    }

    private boolean updateBook(int bookId, String newBookName, String newAuthor, int newQuantity) {
        String sqlQuery = "UPDATE book_details SET book_name = ?, author = ?, quantity = ? WHERE book_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sqlQuery)) {

            pst.setString(1, newBookName);
            pst.setString(2, newAuthor);
            pst.setInt(3, newQuantity);
            pst.setInt(4, bookId);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Error updating book", e);
        }
        return false;
    }

    private boolean deleteBook(int bookId) {
        String sqlQuery = "DELETE FROM book_details WHERE book_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sqlQuery)) {

            pst.setInt(1, bookId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Error deleting book", e);
        }
        return false;
    }

    private void clearTable() {
        if (model != null) {
            model.setRowCount(0);
        }
    }

    private void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(this, message + (e != null ? ": " + e.getMessage() : ""), "Error", JOptionPane.ERROR_MESSAGE);
        if (e != null) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_BookID = new app.bolivia.swing.JCTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_Book_Name = new app.bolivia.swing.JCTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_Author = new app.bolivia.swing.JCTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_Quantity = new app.bolivia.swing.JCTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        manageBookAddBTN = new rojerusan.RSMaterialButtonRectangle();
        manageBookUpdateBTN = new rojerusan.RSMaterialButtonRectangle();
        manageBookDeleteBTN = new rojerusan.RSMaterialButtonRectangle();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_bookDetails = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 17)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/White_Rewind.png"))); // NOI18N
        jLabel1.setText("Back");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 100, 35));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 35));

        txt_BookID.setBackground(new java.awt.Color(102, 102, 255));
        txt_BookID.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_BookID.setForeground(new java.awt.Color(255, 255, 255));
        txt_BookID.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 18)); // NOI18N
        txt_BookID.setPhColor(new java.awt.Color(255, 255, 255));
        txt_BookID.setPlaceholder("Enter Book Id...");
        jPanel1.add(txt_BookID, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 270, -1));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Enter Book ID:");
        jLabel9.setAlignmentY(0.0F);
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 310, 30));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/White_Contact.png"))); // NOI18N
        jLabel3.setAlignmentY(0.0F);
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 50, 60));

        txt_Book_Name.setBackground(new java.awt.Color(102, 102, 255));
        txt_Book_Name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_Book_Name.setForeground(new java.awt.Color(255, 255, 255));
        txt_Book_Name.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 18)); // NOI18N
        txt_Book_Name.setPhColor(new java.awt.Color(255, 255, 255));
        txt_Book_Name.setPlaceholder("Enter Book Name...");
        jPanel1.add(txt_Book_Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 270, -1));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Enter Book Name:");
        jLabel10.setAlignmentY(0.0F);
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 310, 30));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/White_Moleskine.png"))); // NOI18N
        jLabel4.setAlignmentY(0.0F);
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 50, 60));

        txt_Author.setBackground(new java.awt.Color(102, 102, 255));
        txt_Author.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_Author.setForeground(new java.awt.Color(255, 255, 255));
        txt_Author.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 18)); // NOI18N
        txt_Author.setPhColor(new java.awt.Color(255, 255, 255));
        txt_Author.setPlaceholder("Author Name...");
        jPanel1.add(txt_Author, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 270, -1));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Author Name:");
        jLabel11.setAlignmentY(0.0F);
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 310, 30));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/White_Collaborator_Male.png"))); // NOI18N
        jLabel5.setAlignmentY(0.0F);
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 50, 60));

        txt_Quantity.setBackground(new java.awt.Color(102, 102, 255));
        txt_Quantity.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_Quantity.setForeground(new java.awt.Color(255, 255, 255));
        txt_Quantity.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 18)); // NOI18N
        txt_Quantity.setPhColor(new java.awt.Color(255, 255, 255));
        txt_Quantity.setPlaceholder("Quantity...");
        jPanel1.add(txt_Quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 420, 270, -1));

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Quantity:");
        jLabel12.setAlignmentY(0.0F);
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 390, 310, 30));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/White_Unit.png"))); // NOI18N
        jLabel6.setAlignmentY(0.0F);
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 50, 60));

        jPanel3.setBackground(new java.awt.Color(102, 102, 255));

        manageBookAddBTN.setText("Add");
        manageBookAddBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageBookAddBTNMouseClicked(evt);
            }
        });

        manageBookUpdateBTN.setText("Update");
        manageBookUpdateBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageBookUpdateBTNMouseClicked(evt);
            }
        });

        manageBookDeleteBTN.setText("Delete");
        manageBookDeleteBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageBookDeleteBTNMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(manageBookAddBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(manageBookUpdateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(manageBookDeleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manageBookAddBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageBookUpdateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageBookDeleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 400, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 800));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText(" X");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 35, 35));

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 0, 90, 35));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(255, 51, 51));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/Red_Books.png"))); // NOI18N
        jLabel2.setText("Manage Books");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(369, 14, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 51, 51));
        jPanel7.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(347, 80, -1, -1));

        tbl_bookDetails.setAutoCreateRowSorter(true);
        tbl_bookDetails.setBackground(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.setForeground(new java.awt.Color(0, 0, 0));
        tbl_bookDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book ID", "Name", "Author", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_bookDetails.setColorBackgoundHead(new java.awt.Color(102, 102, 255));
        tbl_bookDetails.setColorBordeFilas(new java.awt.Color(102, 102, 255));
        tbl_bookDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        tbl_bookDetails.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        tbl_bookDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_bookDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_bookDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_bookDetails.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.setRowHeight(30);
        tbl_bookDetails.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.getTableHeader().setReorderingAllowed(false);
        tbl_bookDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_bookDetailsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_bookDetails);
        if (tbl_bookDetails.getColumnModel().getColumnCount() > 0) {
            tbl_bookDetails.getColumnModel().getColumn(0).setMinWidth(100);
            tbl_bookDetails.getColumnModel().getColumn(0).setMaxWidth(120);
        }

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 144, 688, 348));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-120, 40, 1025, 768));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 1025, 800));

        setSize(new java.awt.Dimension(1239, 638));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        new HomePage().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
           int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jLabel7MouseClicked

    private void tbl_bookDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_bookDetailsMouseClicked
        int rowNo = tbl_bookDetails.getSelectedRow();
        if (rowNo >= 0) {
            TableModel model = tbl_bookDetails.getModel();
            txt_BookID.setText(model.getValueAt(rowNo, 0).toString());
            txt_Book_Name.setText(model.getValueAt(rowNo, 1).toString());
            txt_Author.setText(model.getValueAt(rowNo, 2).toString());
            txt_Quantity.setText(model.getValueAt(rowNo, 3).toString());
        }
    }//GEN-LAST:event_tbl_bookDetailsMouseClicked

    private void manageBookAddBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageBookAddBTNMouseClicked
        try {
            if (txt_BookID.getText().trim().isEmpty()
                    || txt_Book_Name.getText().trim().isEmpty()
                    || txt_Author.getText().trim().isEmpty()
                    || txt_Quantity.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookId = Integer.parseInt(txt_BookID.getText().trim());
            int quantity = Integer.parseInt(txt_Quantity.getText().trim());

            if (isBookIdExists(bookId)) {
                JOptionPane.showMessageDialog(this, "Book ID already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (addBook()) {
                JOptionPane.showMessageDialog(this, "Book added successfully.");
                clearTable();
                setBookDetailsToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            showError("An unexpected error occurred while adding the book.", e);
        }
    }//GEN-LAST:event_manageBookAddBTNMouseClicked

    private void manageBookUpdateBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageBookUpdateBTNMouseClicked
         int selectedRow = tbl_bookDetails.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = Integer.parseInt(tbl_bookDetails.getValueAt(selectedRow, 0).toString());
            String newBookName = txt_Book_Name.getText().trim();
            String newAuthor = txt_Author.getText().trim();
            int newQuantity = Integer.parseInt(txt_Quantity.getText().trim());

            if (updateBook(bookId, newBookName, newAuthor, newQuantity)) {
                JOptionPane.showMessageDialog(this, "Book updated successfully.");
                clearTable();
                setBookDetailsToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update book.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No book selected for update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_manageBookUpdateBTNMouseClicked

    private void manageBookDeleteBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageBookDeleteBTNMouseClicked
        int selectedRow = tbl_bookDetails.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = Integer.parseInt(tbl_bookDetails.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (deleteBook(bookId)) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                    clearTable();
                    setBookDetailsToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete book.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No book selected for deletion.", "Selection Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_manageBookDeleteBTNMouseClicked

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageBooks().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private rojerusan.RSMaterialButtonRectangle manageBookAddBTN;
    private rojerusan.RSMaterialButtonRectangle manageBookDeleteBTN;
    private rojerusan.RSMaterialButtonRectangle manageBookUpdateBTN;
    private rojeru_san.complementos.RSTableMetro tbl_bookDetails;
    private app.bolivia.swing.JCTextField txt_Author;
    private app.bolivia.swing.JCTextField txt_BookID;
    private app.bolivia.swing.JCTextField txt_Book_Name;
    private app.bolivia.swing.JCTextField txt_Quantity;
    // End of variables declaration//GEN-END:variables
}
