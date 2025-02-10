package com.service;

import com.model.User;
import com.connection.DbConnection;
import java.sql.*;

public class UserService {
    public User login(String username, String password) {
        User user = null;
        try (Connection con = DbConnection.getConnection()) {
            String sql = "SELECT id, name, full_name, password, mobileno, email, role FROM user WHERE name = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("name"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setMobileNo(rs.getString("mobileno"));
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean register(User user) {
        try (Connection con = DbConnection.getConnection()) {
            String sql = "INSERT INTO user (name, full_name, email, mobileno, password, role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getMobileNo());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getRole());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}