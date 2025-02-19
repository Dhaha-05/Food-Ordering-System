package com.util.connection;

import java.sql.*;
public class DbConnection
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/foodapp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Dhaha4326M!";

    private volatile static Connection con;

    public static synchronized Connection getConnection()
    {
        if(con==null)
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        return con;
    }

    public static void closeConnection() throws SQLException {
        if(con!=null)
        {
            con.close();
        }
        else {
            System.out.println("Connection is already closed");
        }
    }
}