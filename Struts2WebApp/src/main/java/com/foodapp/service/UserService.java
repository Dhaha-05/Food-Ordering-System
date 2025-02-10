package com.foodapp.service;

import com.foodapp.model.User;
import com.util.connection.DbConnection;

import java.sql.*;
import java.util.Map;

public class UserService
{
    private Connection con = null;
    private PreparedStatement ps = null;
    public User login(User userBean)
    {
        System.out.println("UserBean : "+userBean);
        if(userBean!=null && !userBean.getUsername().isEmpty() && !userBean.getPassword().isEmpty())
        {
            try
            {
                con = DbConnection.getConnection();
                String query = "SELECT id,username,fullname,email,mobileno,password,role FROM users WHERE username = ? and password = ?";
                ps = con.prepareStatement(query);
                ps.setString(1, userBean.getUsername());
                ps.setString(2, userBean.getPassword());
                ResultSet rs = ps.executeQuery();
                if(!rs.isBeforeFirst())
                {
                    return null;
                }
                if(rs.next())
                {
                    userBean.setUserid(rs.getInt("id"));
                    userBean.setUsername(rs.getString("username"));
                    userBean.setFullname(rs.getString("fullname"));
                    userBean.setEmail(rs.getString("email"));
                    userBean.setMobileno(rs.getString("mobileno"));
                    userBean.setPassword(rs.getString("password"));
                    userBean.setRole(rs.getString("role"));
                    rs.close();
                    ps.close();
                    System.out.println(userBean);
                    return userBean;
                }
            }catch(Exception e)
            {
                System.out.println("Error : "+e.getMessage());
            }
        }
        return null;
    }

    public boolean register(User user) throws SQLException {
        if (user == null) {
            return false;
        }

        try {
            con = DbConnection.getConnection();
            String query = "INSERT INTO users (username, fullname, email, mobileno, password, role) VALUES (?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            System.out.println(user);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getMobileno());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getRole());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected>0)
            {
                ResultSet rs = ps.getGeneratedKeys();
                if(!rs.next())
                {
                    return false;
                }
                else {
                    user.setUserid(rs.getInt(1));
                    return true;
                }
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error : "+e.getMessage());
            return false;
        }
        finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean dataValidation(User user, Map<String,Object> jsonResponse) throws SQLException
    {
        ResultSet result;
        if(user==null)
            return false;
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getFullname().isEmpty() || user.getRole().isEmpty() ||user.getEmail().isEmpty()||user.getMobileno().isEmpty())
        {
            jsonResponse.put("status","error");
            jsonResponse.put("message","Enter All required fields");
            return false;
        }
        else
        {
            boolean isValid=true;
            con = DbConnection.getConnection();
            String sql = "select username from users where username = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            result = ps.executeQuery();
            if(result.isBeforeFirst())
            {
                jsonResponse.put("usernameError","Username not available");
                isValid = false;
            }
            sql = "select email from users where email = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            result = ps.executeQuery();
            if(result.isBeforeFirst())
            {
                jsonResponse.put("emailError","Email already exists");
                isValid = false;
            }
            sql = "select mobileno from users where mobileno = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getMobileno());
            result = ps.executeQuery();
            if(result.isBeforeFirst())
            {
                jsonResponse.put("mobilenoError","Mobile number already exists");
                isValid = false;
            }
            if(!isValid)
            {
                jsonResponse.put("status","failed");
            }
            return isValid;
        }
    }
}
