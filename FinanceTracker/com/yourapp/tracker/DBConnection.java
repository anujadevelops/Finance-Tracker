package com.yourapp.expense;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "DB_URL";
    private static final String USER = "DB_User";
    private static final String PASSWORD = "DB_Pass"; // Your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}