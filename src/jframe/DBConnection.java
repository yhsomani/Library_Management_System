
package jframe;

import java.sql.*;
/**
 *
 * @author ysoma
 */
public class DBConnection {
    static Connection con = null;
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
