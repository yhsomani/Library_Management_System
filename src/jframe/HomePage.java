package jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author ysoma
 */
public class HomePage extends javax.swing.JFrame {

    /**
     * Creates new form HomePage
     */
    Color mouseEnterColor = new Color(0, 0, 0);
    Color mouseExit = new Color(51, 51, 51);
    private DefaultTableModel model;

    public HomePage() {
        initComponents();
        showPieChart();
        setStudentDetailsToTable();
        setBookDetailsToTable();
        setTotalCountOfBooks();
        setTotalCountOfStudent();
        setTotalCountOfIssuedBooks();
        setTotalCountOfDefaulterStudents();
    }
    
    // Method to set and display the total count of defaulter students
    public void setTotalCountOfDefaulterStudents() {
        // Get the current date in the format that matches the SQL date format
        LocalDate today = LocalDate.now();
        String currentDate = today.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // SQL query to count the number of defaulter students
        String sqlQuery = "SELECT COUNT(*) AS TotalDefaulterStudents " +
                          "FROM issue_book_details " +
                          "WHERE status = 'pending' " +
                          "AND due_date < '" + currentDate + "'";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sqlQuery)) {

            // Initialize the total count variable
            int totalDefaulterStudents = 0;

            // Process the result set
            if (rs.next()) {
                totalDefaulterStudents = rs.getInt("TotalDefaulterStudents");
            }

            // Display the total number of defaulter students in the label
            if (TotalCountOfDefaulterStudent != null) {
                TotalCountOfDefaulterStudent.setText("" + totalDefaulterStudents);
            }

        } catch (SQLException e) {
            // Handle errors by calling showError method (assuming it's implemented elsewhere)
            showError("Error fetching defaulter student details", e);
        }
    }
    
    // Method to set and display the total count of issued books with status 'pending'
    public void setTotalCountOfIssuedBooks() {
        // SQL query to count the number of issued books with status 'pending'
        String sqlQuery = "SELECT COUNT(*) AS TotalIssuedBooks FROM issue_book_details WHERE status = 'pending'";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sqlQuery)) {

            // Initialize the total count variable
            int totalIssuedBooks = 0;

            // Process the result set
            if (rs.next()) {
                totalIssuedBooks = rs.getInt("TotalIssuedBooks");
            }

            // Display the total number of issued books in the label
            if (NoOfBookIssued != null) {
                NoOfBookIssued.setText("" + totalIssuedBooks);
            }

        } catch (SQLException e) {
            // Handle errors by calling showError method (assuming it's implemented elsewhere)
            showError("Error fetching issued book details", e);
        }
    }
    
      // Method to set and display the total count of students
    public void setTotalCountOfStudent() {
        // SQL query to count the number of students
        String sqlQuery = "SELECT COUNT(*) AS TotalStudents FROM student_details";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sqlQuery)) {

            // Initialize the total count variable
            int totalStudents = 0;

            // Process the result set
            if (rs.next()) {
                totalStudents = rs.getInt("TotalStudents");
            }

            // Display the total number of students in the label
            if (NoOfStudentAvailable != null) {
                NoOfStudentAvailable.setText("" + totalStudents);
            }

        } catch (SQLException e) {
            // Handle errors by calling showError method (assuming it's implemented elsewhere)
            showError("Error fetching student details", e);
        }
    }

     // Method to set the total count of books
    public void setTotalCountOfBooks() {
        // SQL query to sum the quantity of books
        String sqlQuery = "SELECT SUM(quantity) AS NoOfBookAvailable FROM book_details";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sqlQuery)) {

            // Initialize the total count variable
            int totalBooks = 0;

            // Process the result set
            if (rs.next()) {
                totalBooks = rs.getInt("NoOfBookAvailable");
                // Handle potential null values
                if (rs.wasNull()) {
                    totalBooks = 0;
                }
            }

            // Optionally, you might want to store or use the totalBooks variable
            // For demonstration, we'll just print it
            NoOfBookAvailable.setText(""+totalBooks);

        } catch (SQLException e) {
            // Handle errors by calling showError method (assuming it's implemented elsewhere)
            showError("Error fetching book details", e);
        }
    }
    
    private void setStudentDetailsToTable() {
        String sqlQuery = "SELECT * FROM student_details";
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlQuery)) {

            model = (DefaultTableModel) tblStudentDetails.getModel();
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

    private void setBookDetailsToTable() {
        String sqlQuery = "SELECT * FROM book_details";
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlQuery)) {

            model = (DefaultTableModel) tblBookDetailsTable.getModel();
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

    private void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(this, message + (e != null ? ": " + e.getMessage() : ""), "Error", JOptionPane.ERROR_MESSAGE);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public void showPieChart() {
        // Create a dataset for the pie chart
        DefaultPieDataset pieDataset = new DefaultPieDataset();

        // SQL query to get book details and quantities
        String sqlQuery = "SELECT book_name, quantity FROM book_details";

        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlQuery)) {

            // Iterate through the result set and populate the dataset
            while (rs.next()) {
                String bookName = rs.getString("book_name");
                int quantity = rs.getInt("quantity");
                pieDataset.setValue(bookName + "-" + quantity, quantity);
            }

        } catch (SQLException e) {
            showError("Error fetching book details for pie chart", e);
        }

        // Create the pie chart using the dataset
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Book Details", // Chart title
                pieDataset, // Dataset
                true, // Include legend
                true, // Tooltips
                false // URLs
        );

        // Customize the pie chart
        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(Color.white);
        piePlot.setLabelPaint(Color.GRAY);
        piePlot.setLabelFont(new java.awt.Font("Yu Gothic UI", java.awt.Font.PLAIN, 12));

        // Optionally customize section colors (if desired)
        piePlot.setSectionPaint("Book 1", new Color(255, 255, 102));  // Example, adjust as needed
        piePlot.setSectionPaint("Book 2", new Color(102, 255, 102));  // Example, adjust as needed

        // Create and add the chart panel to the display panel
        ChartPanel chartPanel = new ChartPanel(pieChart);
        panelPieChart.removeAll();
        panelPieChart.add(chartPanel, BorderLayout.CENTER);
        panelPieChart.validate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        CloseScreen = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        manageBooksPanel = new javax.swing.JPanel();
        manageBooksBtn = new javax.swing.JLabel();
        manageStudentsPanel = new javax.swing.JPanel();
        manageStudentsBTN = new javax.swing.JLabel();
        issueBookPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        returnBookPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        viewRecordsPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        viewIssuedBookPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        defaulterListPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        NoOfBookIssued = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        NoOfStudentAvailable = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        TotalCountOfDefaulterStudent = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        NoOfBookAvailable = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        panelPieChart = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBookDetailsTable = new rojeru_san.complementos.RSTableMetro();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblStudentDetails = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1500, 70));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/White_Menu.png"))); // NOI18N
        jLabel1.setAlignmentY(0.0F);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 0, -1, 55));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 11, 5, 33));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/White_User.png"))); // NOI18N
        jLabel2.setText("  Welcome, Admin");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 0, 220, 50));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 21)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Library managment System");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 320, 50));

        CloseScreen.setBackground(new java.awt.Color(255, 255, 255));
        CloseScreen.setFont(new java.awt.Font("MS UI Gothic", 1, 30)); // NOI18N
        CloseScreen.setForeground(new java.awt.Color(255, 255, 255));
        CloseScreen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CloseScreen.setText("X");
        CloseScreen.setAlignmentY(0.0F);
        CloseScreen.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CloseScreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CloseScreenMouseClicked(evt);
            }
        });
        jPanel1.add(CloseScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(1450, 0, 50, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1500, 50));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 51, 51));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel5.setDoubleBuffered(false);
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/White_Home.png"))); // NOI18N
        jLabel6.setText("  Home Page");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 150, 50));

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 340, 50));

        jPanel4.setBackground(new java.awt.Color(102, 102, 255));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel4.setDoubleBuffered(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/White_Exit.png"))); // NOI18N
        jLabel7.setText("  Logout");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 200, 50));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 595, 340, 50));

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Features");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 150, 50));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel7.setDoubleBuffered(false);
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 153, 153));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Library.png"))); // NOI18N
        jLabel8.setText("  LMS Dashboard");
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 200, 50));

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 340, 50));

        manageBooksPanel.setBackground(new java.awt.Color(51, 51, 51));
        manageBooksPanel.setForeground(new java.awt.Color(255, 255, 255));
        manageBooksPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        manageBooksPanel.setDoubleBuffered(false);
        manageBooksPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        manageBooksBtn.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        manageBooksBtn.setForeground(new java.awt.Color(153, 153, 153));
        manageBooksBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Book.png"))); // NOI18N
        manageBooksBtn.setText("  Manage Books");
        manageBooksBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageBooksBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                manageBooksBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                manageBooksBtnMouseExited(evt);
            }
        });
        manageBooksPanel.add(manageBooksBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 290, 50));

        jPanel3.add(manageBooksPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 340, 50));

        manageStudentsPanel.setBackground(new java.awt.Color(51, 51, 51));
        manageStudentsPanel.setForeground(new java.awt.Color(255, 255, 255));
        manageStudentsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        manageStudentsPanel.setDoubleBuffered(false);
        manageStudentsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        manageStudentsBTN.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        manageStudentsBTN.setForeground(new java.awt.Color(153, 153, 153));
        manageStudentsBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Read_Online.png"))); // NOI18N
        manageStudentsBTN.setText("  Manage Students");
        manageStudentsBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageStudentsBTNMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                manageStudentsBTNMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                manageStudentsBTNMouseExited(evt);
            }
        });
        manageStudentsPanel.add(manageStudentsBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 290, 50));

        jPanel3.add(manageStudentsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 265, 340, 50));

        issueBookPanel.setBackground(new java.awt.Color(51, 51, 51));
        issueBookPanel.setForeground(new java.awt.Color(255, 255, 255));
        issueBookPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        issueBookPanel.setDoubleBuffered(false);
        issueBookPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 153, 153));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Sell_26px.png"))); // NOI18N
        jLabel11.setText("  Issue Book");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
        });
        issueBookPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 290, 50));

        jPanel3.add(issueBookPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 340, 50));

        returnBookPanel.setBackground(new java.awt.Color(51, 51, 51));
        returnBookPanel.setForeground(new java.awt.Color(255, 255, 255));
        returnBookPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        returnBookPanel.setDoubleBuffered(false);
        returnBookPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 153, 153));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Return_Purchase.png"))); // NOI18N
        jLabel12.setText("  Return Book");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel12MouseExited(evt);
            }
        });
        returnBookPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 290, 50));

        jPanel3.add(returnBookPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 375, 340, 50));

        viewRecordsPanel.setBackground(new java.awt.Color(51, 51, 51));
        viewRecordsPanel.setForeground(new java.awt.Color(255, 255, 255));
        viewRecordsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        viewRecordsPanel.setDoubleBuffered(false);
        viewRecordsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 153, 153));
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_View_Details.png"))); // NOI18N
        jLabel13.setText("  View Records");
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel13MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel13MouseExited(evt);
            }
        });
        viewRecordsPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 290, 50));

        jPanel3.add(viewRecordsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 340, 50));

        viewIssuedBookPanel.setBackground(new java.awt.Color(51, 51, 51));
        viewIssuedBookPanel.setForeground(new java.awt.Color(255, 255, 255));
        viewIssuedBookPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        viewIssuedBookPanel.setDoubleBuffered(false);
        viewIssuedBookPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(153, 153, 153));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Library.png"))); // NOI18N
        jLabel15.setText("  View Issued Book");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel15MouseExited(evt);
            }
        });
        viewIssuedBookPanel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 290, 50));

        jPanel3.add(viewIssuedBookPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 485, 340, 50));

        defaulterListPanel.setBackground(new java.awt.Color(51, 51, 51));
        defaulterListPanel.setForeground(new java.awt.Color(255, 255, 255));
        defaulterListPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        defaulterListPanel.setDoubleBuffered(false);
        defaulterListPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(153, 153, 153));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Conference.png"))); // NOI18N
        jLabel16.setText("  Defaulter List");
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel16MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel16MouseExited(evt);
            }
        });
        defaulterListPanel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 290, 50));

        jPanel3.add(defaulterListPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 340, 50));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 340, 790));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Issued Books");
        jPanel21.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 5, -1, -1));

        jPanel18.setBackground(new java.awt.Color(204, 204, 204));
        jPanel18.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));

        NoOfBookIssued.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        NoOfBookIssued.setForeground(new java.awt.Color(102, 102, 102));
        NoOfBookIssued.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Sell_50px.png"))); // NOI18N
        NoOfBookIssued.setText("10");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(NoOfBookIssued)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(NoOfBookIssued)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 33, 230, 120));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("No. Of Students");
        jPanel19.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 5, -1, -1));

        jPanel16.setBackground(new java.awt.Color(204, 204, 204));
        jPanel16.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(102, 102, 255)));

        NoOfStudentAvailable.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        NoOfStudentAvailable.setForeground(new java.awt.Color(102, 102, 102));
        NoOfStudentAvailable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_People.png"))); // NOI18N
        NoOfStudentAvailable.setText("10");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(NoOfStudentAvailable)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(NoOfStudentAvailable)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel19.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 33, 230, 120));

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Defaulter List");
        jPanel22.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 5, -1, -1));

        jPanel23.setBackground(new java.awt.Color(204, 204, 204));
        jPanel23.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(102, 102, 255)));

        TotalCountOfDefaulterStudent.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        TotalCountOfDefaulterStudent.setForeground(new java.awt.Color(102, 102, 102));
        TotalCountOfDefaulterStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_List_of_Thumbnails.png"))); // NOI18N
        TotalCountOfDefaulterStudent.setText("10");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(TotalCountOfDefaulterStudent)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(TotalCountOfDefaulterStudent)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel22.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 33, 230, 120));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel17.setBackground(new java.awt.Color(204, 204, 204));
        jPanel17.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));

        NoOfBookAvailable.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        NoOfBookAvailable.setForeground(new java.awt.Color(102, 102, 102));
        NoOfBookAvailable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/Black_Book_Shelf.png"))); // NOI18N
        NoOfBookAvailable.setText("10");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(NoOfBookAvailable)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(NoOfBookAvailable)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel20.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 33, 230, 120));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("No. Of Books");
        jPanel20.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 5, -1, -1));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jPanel6.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 30, 1080, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Students Details");
        jPanel6.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 160, -1));

        panelPieChart.setBackground(new java.awt.Color(204, 204, 204));
        panelPieChart.setLayout(new java.awt.BorderLayout());
        jPanel6.add(panelPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 235, 450, 500));

        tblBookDetailsTable.setAutoCreateRowSorter(true);
        tblBookDetailsTable.setBackground(new java.awt.Color(255, 255, 255));
        tblBookDetailsTable.setForeground(new java.awt.Color(0, 0, 0));
        tblBookDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Clean Code", "Robert C. Martin", "5"}
            },
            new String [] {
                "Book ID", "Name", "Author", "Quantity"
            }
        ));
        tblBookDetailsTable.setColorBackgoundHead(new java.awt.Color(102, 102, 255));
        tblBookDetailsTable.setColorBordeFilas(new java.awt.Color(102, 102, 255));
        tblBookDetailsTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tblBookDetailsTable.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        tblBookDetailsTable.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        tblBookDetailsTable.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tblBookDetailsTable.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tblBookDetailsTable.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tblBookDetailsTable.setRowHeight(30);
        tblBookDetailsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblBookDetailsTable);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 530, 560, 210));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setText("Books Details");
        jPanel6.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 500, 130, -1));

        tblStudentDetails.setAutoCreateRowSorter(true);
        tblStudentDetails.setBackground(new java.awt.Color(255, 255, 255));
        tblStudentDetails.setForeground(new java.awt.Color(0, 0, 0));
        tblStudentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Yash Somani", "BE", "Information Technology"}
            },
            new String [] {
                "Student ID", "Name", "Course", "Branch"
            }
        ));
        tblStudentDetails.setColorBackgoundHead(new java.awt.Color(102, 102, 255));
        tblStudentDetails.setColorBordeFilas(new java.awt.Color(102, 102, 255));
        tblStudentDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tblStudentDetails.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        tblStudentDetails.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        tblStudentDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tblStudentDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tblStudentDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tblStudentDetails.setRowHeight(30);
        tblStudentDetails.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblStudentDetails);

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 560, 210));

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 1160, 790));

        setSize(new java.awt.Dimension(1500, 835));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void CloseScreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseScreenMouseClicked
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);  // Exit the application
        }
    }//GEN-LAST:event_CloseScreenMouseClicked

    private void manageBooksBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageBooksBtnMouseClicked
        ManageBooks book = new ManageBooks();
        book.setVisible(true);
        dispose();
    }//GEN-LAST:event_manageBooksBtnMouseClicked

    private void manageBooksBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageBooksBtnMouseEntered
        manageBooksPanel.setBackground(mouseEnterColor);
    }//GEN-LAST:event_manageBooksBtnMouseEntered

    private void manageBooksBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageBooksBtnMouseExited
        manageBooksPanel.setBackground(mouseExit);
    }//GEN-LAST:event_manageBooksBtnMouseExited

    private void manageStudentsBTNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageStudentsBTNMouseEntered
        manageStudentsPanel.setBackground(mouseEnterColor);
    }//GEN-LAST:event_manageStudentsBTNMouseEntered

    private void manageStudentsBTNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageStudentsBTNMouseExited
        manageStudentsPanel.setBackground(mouseExit);
    }//GEN-LAST:event_manageStudentsBTNMouseExited

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseEntered
        issueBookPanel.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel11MouseEntered

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseExited
        issueBookPanel.setBackground(mouseExit);
    }//GEN-LAST:event_jLabel11MouseExited

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        returnBookPanel.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jLabel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseExited
        returnBookPanel.setBackground(mouseExit);
    }//GEN-LAST:event_jLabel12MouseExited

    private void jLabel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseEntered
        viewRecordsPanel.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel13MouseEntered

    private void jLabel13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseExited
        viewRecordsPanel.setBackground(mouseExit);
    }//GEN-LAST:event_jLabel13MouseExited

    private void jLabel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseEntered
        viewIssuedBookPanel.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel15MouseEntered

    private void jLabel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseExited
        viewIssuedBookPanel.setBackground(mouseExit);
    }//GEN-LAST:event_jLabel15MouseExited

    private void jLabel16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseEntered
        defaulterListPanel.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel16MouseEntered

    private void jLabel16MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseExited
        defaulterListPanel.setBackground(mouseExit);
    }//GEN-LAST:event_jLabel16MouseExited

    private void manageStudentsBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageStudentsBTNMouseClicked
        new ManageStudents().setVisible(true);
        dispose();
    }//GEN-LAST:event_manageStudentsBTNMouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        new IssueBook().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        new ReturnBook().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        new ViewAllRecords().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        new IssuedBookDetails().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        new DefaulterList().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel16MouseClicked

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CloseScreen;
    private javax.swing.JLabel NoOfBookAvailable;
    private javax.swing.JLabel NoOfBookIssued;
    private javax.swing.JLabel NoOfStudentAvailable;
    private javax.swing.JLabel TotalCountOfDefaulterStudent;
    private javax.swing.JPanel defaulterListPanel;
    private javax.swing.JPanel issueBookPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel manageBooksBtn;
    private javax.swing.JPanel manageBooksPanel;
    private javax.swing.JLabel manageStudentsBTN;
    private javax.swing.JPanel manageStudentsPanel;
    private javax.swing.JPanel panelPieChart;
    private javax.swing.JPanel returnBookPanel;
    private rojeru_san.complementos.RSTableMetro tblBookDetailsTable;
    private rojeru_san.complementos.RSTableMetro tblStudentDetails;
    private javax.swing.JPanel viewIssuedBookPanel;
    private javax.swing.JPanel viewRecordsPanel;
    // End of variables declaration//GEN-END:variables
}
