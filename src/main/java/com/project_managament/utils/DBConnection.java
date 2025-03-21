package com.project_managament.utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static DataSource dataSource;

    static {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/demoDB");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize DataSource");
        }
    }

    public static Connection getConnection() throws SQLException {
        long startTime = System.nanoTime(); // Đo thời gian
        Connection conn = dataSource.getConnection();
        long endTime = System.nanoTime();
        System.out.println("Connection Pool Time: " + (endTime - startTime) / 1000000 + " ms");
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}