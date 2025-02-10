package com.service;

import com.model.FoodItem;
import com.connection.DbConnection;
import com.model.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemService {
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        try (Connection con = DbConnection.getConnection()) {
            String sql = "SELECT i.id id, r.name name, i.name item, i.price price, i.category category, i.rating rating, i.offer " +
                    "FROM items i JOIN restaurant r ON r.id = i.restaurant_id;";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FoodItem item = new FoodItem();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setItem(rs.getString("item"));
                item.setPrice(rs.getDouble("price"));
                item.setCategory(rs.getString("category"));
                item.setRating(rs.getString("rating"));
                item.setOffer(rs.getInt("offer"));
                foodItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public List<FoodItem> getFoodItemsByRestaurant(int restaurantId) {
        List<FoodItem> foodItems = new ArrayList<>();
        try (Connection con = DbConnection.getConnection()) {
            String sql = "SELECT i.id id, r.name name, i.name item, i.price price, i.category category, i.rating rating, i.offer " +
                    "FROM items i JOIN restaurant r ON r.id = i.restaurant_id WHERE r.id = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, restaurantId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FoodItem item = new FoodItem();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setItem(rs.getString("item"));
                item.setPrice(rs.getDouble("price"));
                item.setCategory(rs.getString("category"));
                item.setRating(rs.getString("rating"));
                item.setOffer(rs.getInt("offer"));
                foodItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodItems;
    }

//    public boolean createItem(Restaurant r)
//    {
//        return false;
//    }
}