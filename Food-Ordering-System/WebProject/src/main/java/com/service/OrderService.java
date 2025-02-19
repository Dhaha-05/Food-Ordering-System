package com.service;

import com.model.Order;
import com.connection.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    public List<Order> getOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DbConnection.getConnection()) {
            String sql = "SELECT i.id id, i.name name, c.quantity quantity, c.total_price totalPrice, i.price price, c.status " +
                    "FROM orders c JOIN items i ON i.id = c.item_id WHERE user_id = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setItemId(rs.getInt("id"));
                order.setUserId(userId);
                order.setName(rs.getString("name"));
                order.setPrice(rs.getDouble("price"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean placeOrder(Order order) {
        try (Connection con = DbConnection.getConnection()) {
            String sql = "INSERT INTO orders (user_id, item_id, quantity, total_price) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getItemId());
            ps.setInt(3, order.getQuantity());
            ps.setDouble(4, order.getTotalPrice());

            int rowsInserted = ps.executeUpdate();
            if(rowsInserted > 0)
            {
                String query = "Delete from cart where id =  ? ;";
                ps = con.prepareStatement(query);
                ps.setInt(1,order.getCartId());
                int r = ps.executeUpdate();
                if(r>0)
                    System.out.println("item deleted from the cart!!!");
            }
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}