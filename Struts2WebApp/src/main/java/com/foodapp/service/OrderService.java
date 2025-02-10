package com.foodapp.service;

import com.foodapp.model.Order;
import com.util.connection.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private Connection con;
    public List<Order> getOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        try {
            con = DbConnection.getConnection();
            String sql = "SELECT i.id id, i.name name, o.quantity quantity, o.totalprice totalprice, i.price price, o.status " +
                    "FROM orders o JOIN item i ON i.id = o.itemid WHERE userid = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setItemid(rs.getInt("id"));
                order.setUserid(userId);
                order.setItemname(rs.getString("name"));
                order.setPrice(rs.getDouble("price"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotalprice(rs.getDouble("totalprice"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean placeOrder(Order order) {
        System.out.println("Inside Order Service PlaceOrder Method");
        try{
            con = DbConnection.getConnection();
            String sql = "INSERT INTO orders (userid, itemid, quantity, totalprice) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, order.getUserid());
            ps.setInt(2, order.getItemid());
            ps.setInt(3, order.getQuantity());
            ps.setDouble(4, order.getTotalprice());

            int rowsInserted = ps.executeUpdate();
            if(rowsInserted > 0)
            {
                String query = "Delete from cart where id =  ? ;";
                ps = con.prepareStatement(query);
                ps.setInt(1,order.getCartid());
                int r = ps.executeUpdate();
                if(r>0) {
                    System.out.println("item deleted from the cart!!!");
                    return true;
                }
            }
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
