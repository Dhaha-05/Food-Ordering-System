package com.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection
{
    private volatile static Connection con=null;
    public synchronized static Connection getConnection() throws SQLException {
        if(con == null || con.isClosed()) {
            synchronized (DbConnection.class) {
                if (con == null || con.isClosed()) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_ordering", "root", "Dhaha4326M!");
                        System.out.println("Database connection established");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return con;
    }

    public static void closeConnection()
    {
        try {
            if(con!=null && !con.isClosed())
                con.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
