package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_facturacion";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    //private static Connection connection;


    public static Connection getConnection() throws SQLException {
        //Class.forName("com.mysql.cj.jdbc.Driver");  <-- REMOVE THIS LINE.  Driver loading is handled elsewhere or automatically
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}