package jframe;

import java.sql.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ManageStudents extends javax.swing.JFrame {

    private DefaultTableModel model;

    public ManageStudents() {
        initComponents();
        setStudentDetailsToTable();
    }

    private void checkSelectedCourse() {
        String selectedCourse = ComboBoxCourse.getSelectedItem().toString();
        String[] branches = getBranchesForCourse(selectedCourse);
        ComboBoxBranch.setModel(new DefaultComboBoxModel<>(branches));
    }

    private String[] getBranchesForCourse(String course) {
        switch (course) {
            case "B.Tech":
            case "BE":
                return new String[]{
                    "Select Branch", "Computer Science Engineering", "Mechanical Engineering",
                    "Electrical Engineering", "Civil Engineering", "Electronics and Communication Engineering",
                    "Chemical Engineering", "Aerospace Engineering", "Biotechnology Engineering",
                    "Information Technology", "Automobile Engineering", "Marine Engineering"
                };
            case "MBBS":
            case "BDS":
            case "BAMS":
                return new String[]{
                    "Select Branch", "General Medicine", "Pediatrics", "Cardiology", "Orthopedics",
                    "Neurology", "Dentistry", "Ayurveda", "Homeopathy", "Veterinary Medicine", "Surgery"
                };
            case "B.Sc":
            case "M.Sc":
                return new String[]{
                    "Select Branch", "Physics", "Chemistry", "Mathematics", "Biology", "Environmental Science",
                    "Biotechnology", "Computer Science", "Zoology", "Botany", "Microbiology"
                };
            case "B.Com":
            case "M.Com":
                return new String[]{
                    "Select Branch", "Accounting and Finance", "Economics", "Business Studies", "Marketing",
                    "Banking and Insurance", "Taxation", "E-Commerce"
                };
            case "BBA":
            case "MBA":
                return new String[]{
                    "Select Branch", "Finance", "Human Resources", "Marketing", "Operations", "International Business",
                    "Supply Chain Management", "Entrepreneurship"
                };
            case "BA":
            case "MA":
                return new String[]{
                    "Select Branch", "History", "Political Science", "Psychology", "Sociology", "English Literature",
                    "Philosophy", "Economics", "Geography", "Anthropology"
                };
            case "LLB":
            case "LLM":
                return new String[]{
                    "Select Branch", "Corporate Law", "Criminal Law", "Intellectual Property Law", "International Law",
                    "Constitutional Law", "Environmental Law"
                };
            case "B.Des":
            case "M.Des":
                return new String[]{
                    "Select Branch", "Fashion Design", "Interior Design", "Graphic Design", "Product Design",
                    "Textile Design", "Industrial Design"
                };
            case "B.Ed":
            case "M.Ed":
                return new String[]{
                    "Select Branch", "Primary Education", "Secondary Education", "Special Education",
                    "Educational Administration", "Curriculum Design"
                };
            case "B.Arch":
            case "M.Arch":
                return new String[]{
                    "Select Branch", "Urban Design", "Landscape Architecture", "Interior Architecture",
                    "Sustainable Architecture", "Building Technology"
                };
            case "B.Pharm":
            case "M.Pharm":
                return new String[]{
                    "Select Branch", "Pharmaceutical Chemistry", "Pharmacology", "Pharmaceutics", "Pharmacognosy"
                };
            case "BCA":
            case "MCA":
                return new String[]{
                    "Select Branch", "Software Development", "Data Science", "Artificial Intelligence",
                    "Cyber Security", "Network Administration"
                };
            case "BSW":
            case "MSW":
                return new String[]{
                    "Select Branch", "Community Development", "Child Welfare", "Medical and Psychiatric Social Work",
                    "Rural Development", "Human Rights"
                };
            default:
                return new String[]{"Select Branch"};
        }
    }

    private boolean isStudentIdExists(int studentId) {
        String sqlQuery = "SELECT COUNT(*) FROM student_details WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sqlQuery)) {
            pst.setInt(1, studentId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            showError("Error checking student ID existence", e);
        }
        return false;
    }

    private void setStudentDetailsToTable() {
        String sqlQuery = "SELECT * FROM student_details";
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlQuery)) {

            model = (DefaultTableModel) tbl_studentDetails.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getString("course"),
                    rs.getString("branch")
                });
            }
        } catch (SQLException e) {
            showError("Error fetching student details", e);
        }
    }

    private boolean addStudent() {
        String sqlQuery = "INSERT INTO student_details (student_id, student_name, course, branch) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sqlQuery)) {

            int studentId = Integer.parseInt(txt_StudentID.getText().trim());
            String studentName = txt_Student_Name.getText().trim();
            String course = ComboBoxCourse.getSelectedItem().toString();
            String branch = ComboBoxBranch.getSelectedItem().toString();

            if (isStudentIdExists(studentId)) {
                showError("Student ID already exists.", null);
                return false;
            }

            pst.setInt(1, studentId);
            pst.setString(2, studentName);
            pst.setString(3, course);
            pst.setString(4, branch);

            return pst.executeUpdate() > 0;
        } catch (NumberFormatException e) {
            showError("Invalid student ID format.", e);
        } catch (SQLException e) {
            showError("Error adding student", e);
        }
        return false;
    }

    private boolean updateStudent(int studentId, String newStudentName, String newCourse, String newBranch) {
        String sqlQuery = "UPDATE student_details SET student_name = ?, course = ?, branch = ? WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sqlQuery)) {

            pst.setString(1, newStudentName);
            pst.setString(2, newCourse);
            pst.setString(3, newBranch);
            pst.setInt(4, studentId);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Error updating student", e);
        }
        return false;
    }

    private boolean deleteStudent(int studentId) {
        String sqlQuery = "DELETE FROM student_details WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sqlQuery)) {

            pst.setInt(1, studentId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Error deleting student", e);
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
        txt_StudentID = new app.bolivia.swing.JCTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_Student_Name = new app.bolivia.swing.JCTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        manageStudentAddBTN = new rojerusan.RSMaterialButtonRectangle();
        manageStudentUpdateBTN = new rojerusan.RSMaterialButtonRectangle();
        manageStudentDeleteBTN = new rojerusan.RSMaterialButtonRectangle();
        ComboBoxCourse = new javax.swing.JComboBox<>();
        ComboBoxBranch = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_studentDetails = new rojeru_san.complementos.RSTableMetro();

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

        txt_StudentID.setBackground(new java.awt.Color(102, 102, 255));
        txt_StudentID.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_StudentID.setForeground(new java.awt.Color(255, 255, 255));
        txt_StudentID.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 18)); // NOI18N
        txt_StudentID.setPhColor(new java.awt.Color(255, 255, 255));
        txt_StudentID.setPlaceholder("Enter Student Id...");
        jPanel1.add(txt_StudentID, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 270, -1));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Enter Student ID:");
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

        txt_Student_Name.setBackground(new java.awt.Color(102, 102, 255));
        txt_Student_Name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_Student_Name.setForeground(new java.awt.Color(255, 255, 255));
        txt_Student_Name.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 18)); // NOI18N
        txt_Student_Name.setPhColor(new java.awt.Color(255, 255, 255));
        txt_Student_Name.setPlaceholder("Enter Student Name...");
        jPanel1.add(txt_Student_Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 270, -1));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Enter Student Name:");
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

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Select Course");
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

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Select Branch");
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

        manageStudentAddBTN.setText("Add");
        manageStudentAddBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageStudentAddBTNMouseClicked(evt);
            }
        });

        manageStudentUpdateBTN.setText("Update");
        manageStudentUpdateBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageStudentUpdateBTNMouseClicked(evt);
            }
        });

        manageStudentDeleteBTN.setText("Delete");
        manageStudentDeleteBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageStudentDeleteBTNMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(manageStudentAddBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(manageStudentUpdateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(manageStudentDeleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manageStudentAddBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageStudentUpdateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageStudentDeleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 400, -1));

        ComboBoxCourse.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        ComboBoxCourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Course", "B.Tech", "BE", "MBBS", "BDS", "BAMS", "B.Sc", "M.Sc", "B.Com", "M.Com", "BBA", "MBA", "BA", "MA", "LLB", "LLM", "B.Des", "M.Des", "B.Ed", "M.Ed", "B.Arch", "M.Arch", "B.Pharm", "M.Pharm", "BCA", "MCA", "BSW", "MSW" }));
        ComboBoxCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxCourseActionPerformed(evt);
            }
        });
        jPanel1.add(ComboBoxCourse, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 270, -1));

        ComboBoxBranch.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        ComboBoxBranch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Branch" }));
        jPanel1.add(ComboBoxBranch, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 420, 270, -1));

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
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/Red_Student_Male.png"))); // NOI18N
        jLabel2.setText("Manage Students");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(339, 14, -1, -1));

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

        tbl_studentDetails.setAutoCreateRowSorter(true);
        tbl_studentDetails.setBackground(new java.awt.Color(255, 255, 255));
        tbl_studentDetails.setForeground(new java.awt.Color(0, 0, 0));
        tbl_studentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", "Name", "Course", "Branch"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_studentDetails.setColorBackgoundHead(new java.awt.Color(102, 102, 255));
        tbl_studentDetails.setColorBordeFilas(new java.awt.Color(102, 102, 255));
        tbl_studentDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_studentDetails.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        tbl_studentDetails.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        tbl_studentDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_studentDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_studentDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_studentDetails.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_studentDetails.setRowHeight(30);
        tbl_studentDetails.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tbl_studentDetails.getTableHeader().setReorderingAllowed(false);
        tbl_studentDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_studentDetailsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_studentDetails);
        if (tbl_studentDetails.getColumnModel().getColumnCount() > 0) {
            tbl_studentDetails.getColumnModel().getColumn(0).setMinWidth(120);
            tbl_studentDetails.getColumnModel().getColumn(0).setMaxWidth(150);
        }

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 144, 801, 348));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-120, 40, 1025, 768));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 840, 650));

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

    private void tbl_studentDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_studentDetailsMouseClicked
        int rowNo = tbl_studentDetails.getSelectedRow();
        if (rowNo >= 0) {
            TableModel model = tbl_studentDetails.getModel();
            txt_StudentID.setText(model.getValueAt(rowNo, 0).toString());
            txt_Student_Name.setText(model.getValueAt(rowNo, 1).toString());
            ComboBoxCourse.setSelectedItem(model.getValueAt(rowNo, 2).toString());
            ComboBoxBranch.setSelectedItem(model.getValueAt(rowNo, 3).toString());
        }
    }//GEN-LAST:event_tbl_studentDetailsMouseClicked

    private void manageStudentAddBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageStudentAddBTNMouseClicked
        try {
            if (txt_StudentID.getText().trim().isEmpty()
                    || txt_Student_Name.getText().trim().isEmpty()
                    || ComboBoxCourse.getSelectedItem().equals("Select Course")
                    || ComboBoxBranch.getSelectedItem().equals("Select Branch")) {
                showError("Please fill in all fields.", null);
                return;
            }

            if (addStudent()) {
                JOptionPane.showMessageDialog(this, "Student added successfully.");
                clearTable();
                setStudentDetailsToTable();
            } else {
                showError("Failed to add student.", null);
            }
        } catch (Exception e) {
            showError("An unexpected error occurred.", e);
        }
    }//GEN-LAST:event_manageStudentAddBTNMouseClicked

    private void manageStudentUpdateBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageStudentUpdateBTNMouseClicked
        if (txt_StudentID.getText().trim().isEmpty()
                || txt_Student_Name.getText().trim().isEmpty()
                || ComboBoxCourse.getSelectedItem().equals("Select Course")
                || ComboBoxBranch.getSelectedItem().equals("Select Branch")) {
            showError("Please fill in all fields.", null);
            return;
        }

        int selectedRow = tbl_studentDetails.getSelectedRow();
        if (selectedRow >= 0) {
            int studentId = Integer.parseInt(tbl_studentDetails.getValueAt(selectedRow, 0).toString());
            String newName = txt_Student_Name.getText();
            String newCourse = ComboBoxCourse.getSelectedItem().toString();
            String newBranch = ComboBoxBranch.getSelectedItem().toString();

            if (updateStudent(studentId, newName, newCourse, newBranch)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                clearTable();
                setStudentDetailsToTable();
            } else {
                showError("Failed to update student.", null);
            }
        } else {
            showError("No student selected for update.", null);
        }
    }//GEN-LAST:event_manageStudentUpdateBTNMouseClicked

    private void manageStudentDeleteBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageStudentDeleteBTNMouseClicked
        int selectedRow = tbl_studentDetails.getSelectedRow();
        if (selectedRow >= 0) {
            int studentId = Integer.parseInt(tbl_studentDetails.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (deleteStudent(studentId)) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                    clearTable();
                    setStudentDetailsToTable();
                } else {
                    showError("Failed to delete student.", null);
                }
            }
        } else {
            showError("No student selected for deletion.", null);
        }
    }//GEN-LAST:event_manageStudentDeleteBTNMouseClicked

    private void ComboBoxCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxCourseActionPerformed
        checkSelectedCourse();
    }//GEN-LAST:event_ComboBoxCourseActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageStudents().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxBranch;
    private javax.swing.JComboBox<String> ComboBoxCourse;
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
    private rojerusan.RSMaterialButtonRectangle manageStudentAddBTN;
    private rojerusan.RSMaterialButtonRectangle manageStudentDeleteBTN;
    private rojerusan.RSMaterialButtonRectangle manageStudentUpdateBTN;
    private rojeru_san.complementos.RSTableMetro tbl_studentDetails;
    private app.bolivia.swing.JCTextField txt_StudentID;
    private app.bolivia.swing.JCTextField txt_Student_Name;
    // End of variables declaration//GEN-END:variables
}
