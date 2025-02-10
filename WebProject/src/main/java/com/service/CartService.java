package com.service;

import com.model.CartItem;
import com.connection.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        try (Connection con = DbConnection.getConnection()) {
            String sql = "SELECT i.id id, c.id cartid, i.name name, c.quantity quantity, c.total_price total_price, i.price price " +
                    "FROM cart c JOIN items i ON i.id = c.item_id WHERE user_id = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getInt("cartid"));
                item.setUserId(userId);
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setItemId(rs.getInt("id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setTotalPrice(rs.getDouble("total_price"));
                cartItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public boolean addCartItem(CartItem cartItem) {
        try (Connection con = DbConnection.getConnection()) {
            String sql = "INSERT INTO cart (user_id, item_id, quantity, billamount, total_price) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, cartItem.getUserId());
            ps.setInt(2, cartItem.getItemId());
            ps.setInt(3, cartItem.getQuantity());
            ps.setDouble(4,cartItem.getBillamount());
            ps.setDouble(5, cartItem.getTotalPrice());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}