package com.foodapp.service;

import com.util.connection.DbConnection;
import com.foodapp.model.Cart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private Connection con;
    public List<Cart> getCartItems(int userId) {
        List<Cart> cartItems = new ArrayList<>();

        try {
            con = DbConnection.getConnection();
            String sql = "SELECT i.id id, c.id cartid, i.name name, c.quantity quantity, c.totalprice totalprice, i.price price " +
                    "FROM cart c JOIN item i ON i.id = c.itemid WHERE userid = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cart item = new Cart();
                item.setCartid(rs.getInt("cartid"));
                item.setUserid(userId);
                item.setItemname(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setItemid(rs.getInt("id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setTotalprice(rs.getDouble("totalprice"));
                cartItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public boolean addCartItem(Cart cartItem) {
        try {
            con = DbConnection.getConnection();
            String sql = "INSERT INTO cart (userid, itemid, quantity, totalprice) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, cartItem.getUserid());
            ps.setInt(2, cartItem.getItemid());
            ps.setInt(3, cartItem.getQuantity());
            ps.setDouble(4, cartItem.getTotalprice());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}