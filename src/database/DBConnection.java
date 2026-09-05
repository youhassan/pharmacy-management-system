package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String URL = "jdbc:mysql://localhost:3306/PharmacyDB";
    private static String USERNAME = "root";
    private static String PASSWORD = "0000";

    public static Connection getConnection() throws SQLException
    {

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
